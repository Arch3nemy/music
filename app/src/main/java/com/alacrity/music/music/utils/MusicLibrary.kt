package com.alacrity.music.music.utils

import android.annotation.SuppressLint
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.Builder
import com.alacrity.music.utils.runSafely
import com.alacrity.music.music.toProperString
import com.alacrity.music.utils.logThis
import java.util.*

object MusicLibrary {
    private var treeNodes: MutableMap<String, MediaItem> = mutableMapOf()
    var isInitialized = false

    @SuppressLint("UnsafeOptInUsageError")
    private fun buildMediaItem(
        title: String,
        mediaId: String,
        isPlayable: Boolean,
        @MediaMetadata.FolderType folderType: Int,
        album: String? = null,
        artist: String? = null,
        genre: String? = null,
        sourceUri: Uri? = null,
        imageUri: Uri? = null,
    ): MediaItem {
        val metadata = Builder()
            .setAlbumTitle(album)
            .setTitle(title)
            .setArtist(artist)
            .setGenre(genre)
            .setFolderType(folderType)
            .setIsPlayable(isPlayable)
            .setArtworkUri(imageUri)
            .build()
        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setMediaMetadata(metadata)
            .setUri(sourceUri)
            .build().also {
                it.toProperString().logThis("buildMeta")
            }
    }


    @SuppressLint("UnsafeOptInUsageError")
    fun initialize(list: List<MediaItem>) {
        isInitialized = true
        for (element in list) {
            treeNodes[element.mediaId] = element
        }
    }

    fun getAllItems() = treeNodes.values.toList()

    fun getFirstItem(): MediaItem?  = runSafely {
        treeNodes.values.first()
    }.getOrNull()

    fun getRootItem(): MediaItem = treeNodes.values.first()

    fun getRandomItem(): MediaItem? = runSafely {
        treeNodes.values.random()
    }.getOrNull()

    fun getItem(id: String): MediaItem? {
        return treeNodes[id]
    }
}
