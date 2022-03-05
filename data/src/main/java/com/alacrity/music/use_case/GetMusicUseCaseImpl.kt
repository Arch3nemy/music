package com.alacrity.music.use_case

import com.alacrity.music.entity.Audio
import com.alacrity.music.repository.MusicRepository
import javax.inject.Inject

class GetMusicUseCaseImpl @Inject constructor(
    private val repository: MusicRepository
): GetMusicUseCase {

    override suspend fun invoke(): List<Audio> = repository.getMusicFilesFromLocalStorage()

}