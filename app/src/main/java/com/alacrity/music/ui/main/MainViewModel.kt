package com.alacrity.music.ui.main

import com.alacrity.music.ui.main.models.MainEvent
import com.alacrity.music.util.BaseViewModel
import com.alacrity.music.view_states.MainViewState
import com.alacrity.music.view_states.MainViewState.*
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel<MainEvent, MainViewState>(Loading) {

    val viewState: StateFlow<MainViewState> = _viewState

    override fun obtainEvent(event: MainEvent) {
        when (val currentState = _viewState.value) {
            is Loading -> currentState.reduce()
            is Error -> currentState.reduce()
            is FinishedLoading -> currentState.reduce()
            is NoItems -> currentState.reduce()
            is Refreshing -> currentState.reduce()
        }
        _viewState.value = FinishedLoading
    }

    private fun Loading.reduce() {
        logReduce()

    }

    private fun Error.reduce() {
        logReduce()

    }

    private fun FinishedLoading.reduce() {
        logReduce()

    }

    private fun NoItems.reduce() {
        logReduce()

    }

    private fun Refreshing.reduce() {
        logReduce()

    }


}