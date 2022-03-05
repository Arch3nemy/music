package com.alacrity.music.music

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.alacrity.music.exceptions.ControllerNotAvailableException
import com.alacrity.music.music.utils.MusicLibrary
import com.alacrity.music.music.utils.MusicQueue
import com.alacrity.music.utils.logThis
import com.alacrity.music.utils.runSafely
import com.google.common.util.concurrent.MoreExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioController @Inject constructor(
    private val context: Context,
) {

    private lateinit var shuffledQueue: MusicQueue
    private lateinit var legacyQueue: MusicQueue
    private lateinit var queue: MusicQueue
    private var shuffled = false

    private lateinit var controllerFuture: com.google.common.util.concurrent.ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null


    @SuppressLint("UnsafeOptInUsageError")
    fun initializeController(listener: Player.Listener) {
        if (!::controllerFuture.isInitialized)
            controllerFuture =
                MediaController.Builder(
                    context,
                    SessionToken(context, ComponentName(context, PlaybackService::class.java))
                )
                    .buildAsync()
        controllerFuture.addListener({ setController(listener) }, MoreExecutors.directExecutor())
    }

    fun isInitialized() = MusicLibrary.isInitialized

    fun releaseController(onError: (Throwable) -> Unit) {
        runSafely(onError = onError) {
            MediaController.releaseFuture(controllerFuture)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun sendIntent(intent: Intent) {
        controller?.sendCustomCommand(SessionCommand("CUSTOM_ACTION", Bundle()), Bundle().apply {
            putParcelable("Intent", intent)
        })
    }

    fun getCurrentIndex(): Int {
        if (!::queue.isInitialized) return -1
        if (shuffled) return getShuffledIndexInLegacyQueue()
        return queue.getCurrentIndex().also { it.logThis("Current index") }
    }

    fun isPlaying(): Result<Boolean> {
        return runSafely {
            controller!!.isPlaying
        }
    }

    fun getPlayingTime(): Long {
        controller ?: throw ControllerNotAvailableException("Audio controller is not available")
        return controller!!.currentPosition
    }

    private fun setController(listener: Player.Listener) {
        val controller = this.controller ?: return
        controller.addListener(listener)
        controller.playWhenReady = true
    }

    fun initQueue(list: List<MediaItem>) {
        legacyQueue = MusicQueue(list as MutableList<MediaItem>)
        shuffledQueue = legacyQueue.copy()
        queue = legacyQueue
        shuffled = queue === shuffledQueue
    }

    fun playFromIndex(index: Int, onError: (Throwable) -> Unit, onSuccess: (() -> Unit)? = null) {
        runSafely(onError = onError, onSuccess = { onSuccess?.invoke() }) {
            if (getCurrentIndex() == index) return@runSafely
            if (shuffled) unShuffle()
            queue.playFromIndex(index)?.let {
                controller?.setMediaItem(it)
                controller?.prepare()
            }
        }
    }

    fun shuffle(
        reactInstantly: Boolean,
        onError: (Throwable) -> Unit,
        onSuccess: (() -> Unit)? = null
    ) {
        runSafely(onError = onError, onSuccess = { onSuccess?.invoke() }) {
            shuffled = true
            shuffledQueue.shuffle()
            queue = shuffledQueue

            if (reactInstantly)
                queue.playFromIndex(0)?.let {
                    controller?.setMediaItem(it)
                    controller?.prepare()
                }
        }
    }

    fun unShuffle() {
        queue = legacyQueue
        shuffled = false
    }

    fun playPauseAudio(onError: (Throwable) -> Unit, onSuccess: (() -> Unit)? = null) {
        runSafely(onError = onError, onSuccess = { onSuccess?.invoke() }) {
            if (controller == null) throw ControllerNotAvailableException("Audio controller is not available")

            if (queue.getCurrentAudio() == null)
                playFromIndex(0, onError).also { return@runSafely }

            if (controller?.isPlaying == true)
                controller?.pause()
            else
                controller?.play()
        }
    }

    fun nextAudio(onError: (Throwable) -> Unit, onSuccess: (() -> Unit)? = null) {
        runSafely(onError = onError, onSuccess = { onSuccess?.invoke() }) {
            if (controller == null) throw ControllerNotAvailableException("Audio controller is not available")
            prepareAndPlayAudio(queue.getNextAudioToPlay())
        }
    }

    private fun prepareAndPlayAudio(mediaItem: MediaItem) {
        controller?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }

    fun prevAudio(onError: (Throwable) -> Unit, onSuccess: (() -> Unit)? = null) {
        runSafely(onError = onError, onSuccess = { onSuccess?.invoke() }) {
            if (controller == null) throw ControllerNotAvailableException("Audio controller is not available")
            prepareAndPlayAudio(queue.getPrevAudioToPlay())
        }
    }

    private fun getShuffledIndexInLegacyQueue(): Int {
        val item = shuffledQueue.getCurrentAudio()
        legacyQueue.getData().forEachIndexed { index, audio ->
            if (item === audio) return index
        }
        return -1
    }
}

@SuppressLint("UnsafeOptInUsageError")
fun MediaItem.toProperString() = StringBuilder().apply {
    appendLine("id | $mediaId")
    appendLine("artist | ${mediaMetadata.artist}")
    appendLine("albumTitle | ${mediaMetadata.albumTitle}")
    appendLine("displayTitle | ${mediaMetadata.displayTitle}")
    appendLine("description | ${mediaMetadata.description}")
    appendLine("mediaUri | ${mediaMetadata.mediaUri}")
    appendLine("genre | ${mediaMetadata.genre}")
    appendLine("folderType | ${mediaMetadata.folderType}")
    appendLine("real uri | ${this@toProperString.localConfiguration?.uri}")
    appendLine("meta | $mediaMetadata")
}.toString()


