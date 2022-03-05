package com.alacrity.music.music

import android.annotation.SuppressLint
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.*
import com.alacrity.music.core.MainActivity
import com.alacrity.music.exceptions.MusicException
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.alacrity.music.music.utils.MusicLibrary
import com.alacrity.music.utils.logThis
import javax.inject.Inject

class PlaybackService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaLibrarySession: MediaLibrarySession
    private val librarySessionCallback = CustomMediaLibrarySessionCallback()

    @Inject
    lateinit var audioController: AudioController


    private inner class CustomMediaLibrarySessionCallback :
        MediaLibrarySession.MediaLibrarySessionCallback {

        /*  override fun onCustomCommand(
              session: MediaSession,
              controller: MediaSession.ControllerInfo,
              customCommand: SessionCommand,
              args: Bundle
          ): ListenableFuture<SessionResult> {
              intent = args.getParcelable("Intent")
              return super.onCustomCommand(session, controller, customCommand, args)
          }*/

        @SuppressLint("UnsafeOptInUsageError")
        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            return Futures.immediateFuture(LibraryResult.ofItem(MusicLibrary.getRootItem(), params))
        }


        @SuppressLint("UnsafeOptInUsageError")
        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val item =
                MusicLibrary.getItem(mediaId)
                    ?: return Futures.immediateFuture(
                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
                    )
            return Futures.immediateFuture(LibraryResult.ofItem(item, null))
        }


        private fun setMediaItemFromSearchQuery(query: String) {
            val mediaTitle =
                if (query.startsWith("play ", ignoreCase = true)) {
                    query.drop(5)
                } else {
                    query
                }

            val item = MusicLibrary.getRandomItem()
            item?.let {
                player.setMediaItem(it)
                player.prepare()
            }
        }

        @SuppressLint("UnsafeOptInUsageError")
        override fun onSetMediaUri(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            uri: Uri,
            extras: Bundle
        ): Int {

            if (uri.toString().startsWith(SEARCH_QUERY_PREFIX) ||
                uri.toString().startsWith(SEARCH_QUERY_PREFIX_COMPAT)
            ) {
                val searchQuery =
                    uri.getQueryParameter("query") ?: return SessionResult.RESULT_ERROR_NOT_SUPPORTED
                setMediaItemFromSearchQuery(searchQuery)

                return SessionResult.RESULT_SUCCESS
            } else {
                return SessionResult.RESULT_ERROR_NOT_SUPPORTED
            }
        }
    }

    private class CustomMediaItemFiller :
        MediaSession.MediaItemFiller {
        @SuppressLint("UnsafeOptInUsageError")
        override fun fillInLocalConfiguration(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItem: MediaItem
        ): MediaItem {
            return (MusicLibrary.getItem(mediaItem.mediaId) ?: throw MusicException("FUCK")).also { it.toProperString().logThis("fill in local config: Media Item") }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    override fun onDestroy() {
        player.release()
        mediaLibrarySession.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
        return mediaLibrarySession
    }



    @SuppressLint("UnsafeOptInUsageError")
    private fun initializeSessionAndPlayer() {
        player =
            ExoPlayer.Builder(this)
                .setAudioAttributes(AudioAttributes.DEFAULT, true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_LOCAL)
                .build()

        val pendingIntent =
            TaskStackBuilder.create(this).run {
                addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))


                getPendingIntent(0, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
            }

        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, librarySessionCallback)
                .setMediaItemFiller(CustomMediaItemFiller())
                .setSessionActivity(pendingIntent)
                .build()
    }

companion object {
    private const val SEARCH_QUERY_PREFIX_COMPAT = "androidx://media3-session/playFromSearch"
    private const val SEARCH_QUERY_PREFIX = "androidx://media3-session/setMediaUri"
}

}

