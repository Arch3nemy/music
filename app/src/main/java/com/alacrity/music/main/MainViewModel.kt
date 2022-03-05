package com.alacrity.music.main

import com.alacrity.music.entity.Audio
import com.alacrity.music.main.models.MainEvent
import com.alacrity.music.main.ui.PlayingState
import com.alacrity.music.music.AudioController
import com.alacrity.music.music.utils.MusicLibrary
import com.alacrity.music.use_case.GetMusicUseCase
import com.alacrity.music.util.BaseViewModel
import com.alacrity.music.utils.logThis
import com.alacrity.music.utils.toMediaItemList
import com.alacrity.music.view_states.MainViewState
import com.alacrity.music.view_states.MainViewState.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getMusicUseCase: GetMusicUseCase,
    private val controller: AudioController,
) : BaseViewModel<MainEvent, MainViewState>(Loading) {

    private val musicList = ArrayList<Audio>()
    val viewState: StateFlow<MainViewState> = baseModelViewState
    val permissionState = MutableStateFlow(false)
    val shuffleState = MutableStateFlow(false)

    private val _playingState: MutableStateFlow<PlayingState> =
        MutableStateFlow(PlayingState.InitialState)
    val playingState: StateFlow<PlayingState> = _playingState


    override fun obtainEvent(event: MainEvent) {
        when (val currentState = baseModelViewState.value) {
            is Loading -> currentState.reduce(event)
            is Error -> currentState.reduce(event)
            is SongScreen -> currentState.reduce(event)
            is NoItems -> currentState.reduce(event)
            is Refreshing -> currentState.reduce(event)
            is MainScreen -> currentState.reduce(event)
        }
    }

    private fun MainScreen.reduce(event: MainEvent) {
        logReduce(event)
        when (event) {
            is MainEvent.ItemClick -> {
                controller.playFromIndex(event.index, onError = { showError(exception = it) }) {
                    changeShuffleState(false)
                    openSongView()
                }
            }
            MainEvent.ClickPlayPauseAudio -> controller.playPauseAudio(onError = {
                showError(
                    exception = it
                )
            })
            is MainEvent.Shuffle -> {
                controller.shuffle(event.reactInstantly, onError = { showError(exception = it) }) {
                    changeShuffleState(true)
                    openSongView()
                }
            }
            MainEvent.ClickNextAudio -> controller.nextAudio(onError = { showError(exception = it) })

            is MainEvent.Scroll -> launch {
                event.state.scrollToItem(playingState.value.index)
            }
            else -> Unit
        }
    }

    private fun Loading.reduce(event: MainEvent) {
        logReduce(event)
        when (event) {
            is MainEvent.PermissionsAccepted -> {
                val currentIndex = controller.getCurrentIndex()
                val isPlaying = controller.isPlaying()
                isPlaying.fold(
                    onSuccess = {
                        if(currentIndex >= 0) {
                            if(it)
                                changePlaybackState(PlayingState.Playing(currentIndex))
                            else
                                changePlaybackState(PlayingState.Paused(currentIndex))
                        }

                    },
                    onFailure = {
                        it.logThis("First launch: ignored error")
                    }
                )
                permissionState.value = true
                loadData()
            }
            else -> Unit
        }
    }

    private fun Error.reduce(event: MainEvent) {
        logReduce(event)
        when (event) {
            is MainEvent.RestartApp -> {
                logThis("OnRestartApplication")
            }
            else -> Unit
        }
    }

    private fun SongScreen.reduce(event: MainEvent) {
        logReduce(event).also {
            if (event is MainEvent.ClickToggleShuffle) event.logThis(
                shuffleState.value.toString()
            )
        }
        when (event) {
            MainEvent.ClickNextAudio -> controller.nextAudio(onError = { showError(exception = it) })
            MainEvent.ClickPlayPauseAudio -> controller.playPauseAudio(onError = {
                showError(
                    exception = it
                )
            })
            MainEvent.ClickPrevAudio -> controller.prevAudio(onError = { showError(exception = it) })
            is MainEvent.ClickToggleShuffle -> {
                clickToggleShuffle()
            }
            MainEvent.Kekw -> {
                controller.getPlayingTime()
                musicList[controller.getCurrentIndex()].duration.logThis("full duration")
            }
            else -> Unit
        }
    }

    private fun NoItems.reduce(event: MainEvent) {
        logReduce(event)

    }

    private fun Refreshing.reduce(event: MainEvent) {
        logReduce(event)

    }

    fun showError(message: String = "Error occurred", exception: Throwable? = null) {
        exception.logThis(message)
        baseModelViewState.value = Error(message, exception)
    }

    fun changePlaybackState(state: PlayingState) {
        _playingState.value = state
    }

    private fun changeShuffleState(state: Boolean) {
        shuffleState.value = state
    }

    private fun changeViewState(state: MainViewState) {
        baseModelViewState.value = state
    }

    fun backToMainScreen() {
        baseModelViewState.value = MainScreen(musicList)
    }

    private fun loadData() {
        launch {
            safeCall(
                successLogs = "Successfully loaded audio",
                errorLogs = "Error while loading audio"
            ) {
                getMusicUseCase()
            }.fold(
                onSuccess = { music ->
                    initMusicLibrary(music)
                    setMusicAndViewState(music)
                    initControllerQueue(music)
                },
                onFailure = {
                    showError("Error while loading audio", it)
                }
            )

            playingState.collect {
                it.logThis("Collecting State")
            }

        }
    }

    /**
     * Opens song view with delay
     * @param withDelay: when opening for the first time
     * index is needed for viewpager to setUp pages
     */
    private fun openSongView(withDelay: Boolean = true) {
        val delayBeforeOpen = if (withDelay) 100L else 0
        launch {
            delay(delayBeforeOpen)
            baseModelViewState.value = SongScreen(musicList)
        }
    }

    private fun initMusicLibrary(music: List<Audio>) {
        MusicLibrary.initialize(music.toMediaItemList())
        music.logThis("toMediaItemList")
    }

    private fun initControllerQueue(music: List<Audio>) {
        launch {
            safeCall {
                music.toMediaItemList()
            }.foldOnMainDispatcher(onSuccess = {
                controller.initQueue(it)
            }, onFailure = {
                showError("Error initializing controller queue", it)
            })
        }
    }

    private fun setMusicAndViewState(music: List<Audio>) {
        musicList.clear()
        musicList.addAll(music)
        if (music.isEmpty()) {
            baseModelViewState.value = NoItems
        } else {
            baseModelViewState.value = MainScreen(music = music)
        }
    }

    private fun clickToggleShuffle() {
        val shuffled = shuffleState.value
        changeShuffleState(!shuffled)
        if (shuffleState.value)
            controller.shuffle(
                reactInstantly = false,
                onError = { showError(exception = it) })
        else
            controller.unShuffle()
        changeShuffleState(!shuffled)

        shuffleState.value.logThis("OnClickToggleShuffle result")
    }
}