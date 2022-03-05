package com.alacrity.music.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.alacrity.music.main.MainViewModel
import com.alacrity.music.main.models.nextAudio
import com.alacrity.music.main.ui.PlayingState
import com.alacrity.music.utils.ifInited
import com.alacrity.music.utils.logThis
import com.alacrity.music.music.AudioController
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var audioController: AudioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        if(isFinishing) return
        setContent {
            MusicApp(context = this, viewModel = mainViewModel)
        }
    }

    private val listener = @SuppressLint("UnsafeOptInUsageError")
    object : Player.Listener {

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            mediaMetadata.logThis("onMediaMetadataChanged")
            val index = ::audioController.ifInited(audioController.getCurrentIndex(), -1)
                .also { it.logThis("onMediaMetadataChanged index") }
            mainViewModel.changePlaybackState(PlayingState.Playing(index))

        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            playbackState.logThis("onPlaybackStateChanged")
            val index = ::audioController.ifInited(audioController.getCurrentIndex(),  -1)
                .also { it.logThis("onPlaybackStateChanged index") }
            val state = when (playbackState) {
                Player.STATE_READY -> {
                    if(index >= 0) PlayingState.Playing(index) else PlayingState.InitialState
                }
                else -> PlayingState.InitialState
            }
            if (playbackState == ExoPlayer.STATE_ENDED) {
                mainViewModel.nextAudio()
            }
            mainViewModel.changePlaybackState(state).also { state.logThis("changePlaybackState onPlaybackStateChanged") }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            super.onPlayWhenReadyChanged(playWhenReady, reason)
            playWhenReady.logThis("onPlayWhenReadyChanged | reason $reason")
            val index = ::audioController.ifInited(audioController.getCurrentIndex(),  -1)
                .also { it.logThis("onPlayWhenReadyChanged index") }
            val state = when (playWhenReady) {
                true -> if(index != -1) PlayingState.Playing(index) else PlayingState.InitialState
                false -> PlayingState.Paused(index)
            }
            mainViewModel.changePlaybackState(state).also { state.logThis("changePlaybackState onPlayWhenReadyChanged") }

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            playbackState.logThis("onPlayerStateChanged")

        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            error.logThis("onPlayerError")
            mainViewModel.showError(exception = error)
        }


    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenCreated {
            mainViewModel.permissionState.collect { accepted ->
                val index = audioController.getCurrentIndex().also { it.logThis("OnStart") }
                if(accepted && index < 0)
                    audioController.initializeController(listener)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        "Onstop".logThis()
        audioController.releaseController {
            it.logThis("Error while releasing controller")
        }
    }


}