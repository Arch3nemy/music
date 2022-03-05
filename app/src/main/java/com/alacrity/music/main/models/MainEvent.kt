package com.alacrity.music.main.models

import androidx.compose.foundation.lazy.LazyListState
import com.alacrity.music.base.BaseEvent
import com.alacrity.music.entity.Audio
import com.alacrity.music.main.MainViewModel

sealed class MainEvent: BaseEvent {

    object EnterScreen: MainEvent()

    object PermissionsAccepted: MainEvent()

    object ClickNextAudio: MainEvent()

    object ClickPrevAudio: MainEvent()

    object ClickPlayPauseAudio: MainEvent()

    object ClickToggleShuffle: MainEvent()

    object RestartApp: MainEvent()

    object Kekw: MainEvent()

    data class Scroll(val state: LazyListState): MainEvent()

    data class Shuffle(val reactInstantly: Boolean): MainEvent()

    data class ItemClick(val index: Int, val audio: Audio): MainEvent()
}

fun MainViewModel.enterScreen() {
    obtainEvent(MainEvent.EnterScreen)
}

fun MainViewModel.onPermissionsAccepted() {
    obtainEvent(MainEvent.PermissionsAccepted)
}

fun MainViewModel.nextAudio() {
    obtainEvent(MainEvent.ClickNextAudio)
}

fun MainViewModel.prevAudio() {
    obtainEvent(MainEvent.ClickPrevAudio)
}

fun MainViewModel.playPause() {
    obtainEvent(MainEvent.ClickPlayPauseAudio)
}

fun MainViewModel.itemClick(index: Int, item: Audio) {
    obtainEvent(MainEvent.ItemClick(index, item))
}

fun MainViewModel.shuffle(reactInstantly: Boolean) {
    obtainEvent(MainEvent.Shuffle(reactInstantly))
}

fun MainViewModel.openPlaylist() {
    //TODO
}

fun MainViewModel.restartClick() {
    obtainEvent(MainEvent.RestartApp)
}

fun MainViewModel.toggleShuffle() {
    obtainEvent(MainEvent.ClickToggleShuffle)
}

fun MainViewModel.scrollToPos(state: LazyListState) {
    obtainEvent(MainEvent.Scroll(state))
}

