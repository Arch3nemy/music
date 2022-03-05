package com.alacrity.music.use_case

import com.alacrity.music.entity.Audio

interface GetMusicUseCase {

    suspend operator fun invoke(): List<Audio>

}