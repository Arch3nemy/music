package com.alacrity.music.main.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.alacrity.music.main.MainViewModel
import com.alacrity.music.main.models.*
import com.alacrity.music.util.HandlePermissions
import com.alacrity.music.util.customShape
import com.alacrity.music.util.getScreenSize
import com.alacrity.music.view_states.MainViewState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    context: Context,
    viewModel: MainViewModel,
    navController: NavController
) {

    val state by viewModel.viewState.collectAsState()
    val playbackState by viewModel.playingState.collectAsState()
    val shuffled by viewModel.shuffleState.collectAsState()
    val scope = rememberCoroutineScope()

    MainWrapper(viewModel = viewModel) {
        when (state) {
            MainViewState.Loading -> {
                LoadingScreen()
            }
            is MainViewState.NoItems -> {

            }
            is MainViewState.Error -> {
                val errorState = state as MainViewState.Error
                ErrorView(errorState.exception, errorState.message) {
                    viewModel.restartClick()
                }
            }

            is MainViewState.Refreshing -> {

            }

            is MainViewState.MainScreen -> {
                val mainState = state as MainViewState.MainScreen
                MainScreen(audioFiles = mainState.music,
                    playbackState = playbackState,
                    onShuffle = { viewModel.shuffle(true) },
                    onClickPlayPause = { viewModel.playPause() },
                    onItemClick = { index, audio ->
                        viewModel.itemClick(index, audio); viewModel.viewState.value
                    },
                    onClickFavourites = { /*TODO*/ },
                    onClickPlaylist = { /*TODO*/scope.launch {
                        if (playbackState.index != -1)
                            it.scrollToItem(playbackState.index, scrollOffset = 5)
                    }
                    }
                )
            }
            is MainViewState.SongScreen -> {
                SongScreen(
                    audioList = (state as MainViewState.SongScreen).audioList,
                    playbackState = playbackState,
                    shuffleState = shuffled,
                    onClickPrev = { viewModel.prevAudio() },
                    onClickNext = { viewModel.nextAudio() },
                    onClickPlayPause = { viewModel.playPause() },
                    onClickPlaylist = { /*TODO*/viewModel.obtainEvent(MainEvent.Kekw) },
                    onClickMore = { /*TODO*/ },
                    onClickBack = { viewModel.backToMainScreen() },
                    onToggleShuffle = { viewModel.toggleShuffle() }
                )
            }
        }

        val activity = (LocalContext.current as? Activity)

        BackHandler {
            if (state is MainViewState.SongScreen) {
                viewModel.backToMainScreen()
            } else {
                activity?.onBackPressed()
            }
        }

        LaunchedEffect(key1 = state, block = {
            viewModel.enterScreen()
        })

    }
}

@Composable
fun MainWrapper(viewModel: MainViewModel, content: @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    with(getScreenSize<Float>()) {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                /*AppDrawer(
                    closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                    width = (first / 5 - 20).dp,
                    refresh = { viewModel.obtainEvent(HomeEvent.OnRefreshClicked) }
                )*/
            },
            drawerShape = customShape(first, second),
        ) {
            HandlePermissions(onPermissionsAccepted = { viewModel.onPermissionsAccepted() }) {
                content()
            }
        }
    }
}