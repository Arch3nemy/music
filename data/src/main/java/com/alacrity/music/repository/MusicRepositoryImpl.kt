package com.alacrity.music.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import com.alacrity.music.data.R
import com.alacrity.music.entity.Audio
import com.alacrity.music.utils.getIndex
import com.alacrity.music.utils.logThis
import com.alacrity.music.utils.toMediaItemList
import javax.inject.Inject


class MusicRepositoryImpl @Inject constructor(
    private val context: Context,
) : MusicRepository {

    private val selection = AudioColumns.IS_MUSIC + " != 0";

    private val projection = arrayOf(
        AudioColumns._ID,
        AudioColumns.ARTIST,
        AudioColumns.TITLE,
        AudioColumns.DATA,
        AudioColumns.DISPLAY_NAME,
        AudioColumns.DURATION,
        AudioColumns.ALBUM_ID
    )

    override suspend fun getMusicFilesFromLocalStorage(): List<Audio> {
        logThis("getMusicFilesFromLocalStorage")
        val cursor = context.contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null, AudioColumns.DATE_ADDED + " DESC",
        )

        val songs = mutableListOf<Audio>()

        return try {
            if (cursor != null && cursor.moveToFirst())
                do {
                    cursor.run {
                        val id = getString(getIndex(AudioColumns._ID))
                        val artist = getString(getIndex(AudioColumns.ARTIST))
                        val title = getString(getIndex(AudioColumns.TITLE))
                        val path = getString(getIndex(AudioColumns.DATA))
                        val displayName =
                            getString(getIndex(AudioColumns.DISPLAY_NAME))
                        val duration = getLong(getIndex(AudioColumns.DURATION))
                        val albumId =
                            getLong(getIndex(AudioColumns.ALBUM_ID))

                        songs.add(
                            Audio(
                                id = id,
                                artist = artist,
                                title = title,
                                path = path,
                                displayName = displayName,
                                duration = duration,
                                albumId = albumId,
                                albumArt = ContentUris.withAppendedId(
                                    Uri.parse(
                                        context.resources.getString(
                                            R.string.album_art_dir
                                        )
                                    ), albumId
                                )
                            )
                        )
                    }
                } while (cursor.moveToNext())
            songs
        } catch (e: Exception) {
            emptyList()
        } finally {
            cursor?.close()
        }
    }
}