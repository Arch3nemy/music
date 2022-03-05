package com.alacrity.music.music.utils

import androidx.media3.common.MediaItem

data class MusicQueue(private val data: MutableList<MediaItem>) {

    private var currentIndex = -1
    private val lastIndex = data.size - 1

    fun getCurrentIndex() = currentIndex

    fun getData() = data

    fun getCurrentAudio(): MediaItem? = try {
        data[currentIndex]
    } catch (e: IndexOutOfBoundsException) {
        null
    }

    fun getNextAudioToPlay(): MediaItem {
        currentIndex++
        if (currentIndex < 0 || currentIndex >= data.size) return data[0]
        return data[currentIndex]
    }

    fun getPrevAudioToPlay(): MediaItem {
        currentIndex--
        if (currentIndex < 0) return data[lastIndex]
        return data[currentIndex]
    }

    fun playFromIndex(index: Int): MediaItem? {
        return try {
            data[index].also { currentIndex = index }
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun shuffle() {
        data.shuffle()
    }

    fun copy(): MusicQueue {
        val list = ArrayList<MediaItem>()
        list.addAll(data)
        return MusicQueue(list)
    }
}
