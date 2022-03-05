package com.alacrity.music.core.di

import com.alacrity.music.use_case.GetMusicUseCase
import com.alacrity.music.use_case.GetMusicUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

    @Binds
    fun bindProvideCheckDateUseCase(impl: GetMusicUseCaseImpl): GetMusicUseCase

}