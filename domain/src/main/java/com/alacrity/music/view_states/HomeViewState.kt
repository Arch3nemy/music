package com.alacrity.music.view_states

import com.alacrity.music.entity.Audio

sealed class MainViewState: BaseViewState {
    object Loading : MainViewState()
    object Refreshing: MainViewState()
    object NoItems: MainViewState()

    data class Error(val message: String = "", val exception: Throwable? = null) : MainViewState()
    data class MainScreen(val music: List<Audio>): MainViewState()
    data class SongScreen(val audioList: List<Audio>): MainViewState()
}

