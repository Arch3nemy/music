package com.alacrity.music.utils

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alacrity.music.entity.Audio
import timber.log.Timber
import java.io.File

@SuppressLint("UnsafeOptInUsageError")
fun buildMediaItem(
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
    val metadata =
        MediaMetadata.Builder()
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
        .build()
}

fun List<Audio>.toMediaItemList(): List<MediaItem> {
    val result = mutableListOf<MediaItem>()
    forEach {
        result.add(it.toMediaItem())
    }
    return result
}

fun Cursor.getIndex(p0: String): Int =
    if (getColumnIndex(p0) > 0) getColumnIndex(p0) else 0


/**
 * TODO add new fields
 * @param imageUri
 * @param album
 * @param genre
 */
@SuppressLint("UnsafeOptInUsageError")
fun Audio.toMediaItem() = buildMediaItem(
    title = title,
    mediaId = id,
    isPlayable = true,
    folderType = MediaMetadata.FOLDER_TYPE_NONE,
    album = null,
    artist = artist,
    genre = null,
    sourceUri = Uri.parse(File(path).toString()),
    imageUri = albumArt as Uri?
)

const val DEFAULT_PREFIX = "LOGX"


/**
 * Logs any object
 * DEFAULT_PREFIX is application prefix to see all the logs of
 * current application
 * @param prefix is prefix for this exact log
 * @sample logSample
 **/
fun Any?.logThis(prefix: String = "") {
    Timber.d("$DEFAULT_PREFIX $prefix: $this")
}

fun logSample() {
    Pair("Sample string", 4).logThis("Pair of two values")
    "LogStringWithoutPrefix".logThis()
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isVersionQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}