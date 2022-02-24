package com.alacrity.music.view_states


sealed class MainViewState: BaseViewState {
    object Loading : MainViewState()
    object Refreshing: MainViewState()
    data class Error(val exception: Throwable? = null, val message: String = "") : MainViewState()
    object NoItems: MainViewState()
    object FinishedLoading : MainViewState()
}