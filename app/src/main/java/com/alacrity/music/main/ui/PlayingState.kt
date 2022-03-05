package com.alacrity.music.main.ui

sealed class PlayingState(val isPlaying: Boolean, val index: Int) {

    data class Playing(val curIndex: Int): PlayingState(isPlaying = true, index = curIndex)

    data class Paused(val curIndex: Int): PlayingState(isPlaying = false, index = curIndex)

    object InitialState: PlayingState(isPlaying = false, index = -1)

}
