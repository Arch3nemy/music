package com.alacrity.music.repository

import com.alacrity.music.entity.Audio


interface MusicRepository {

    suspend fun getMusicFilesFromLocalStorage(): List<Audio>


}