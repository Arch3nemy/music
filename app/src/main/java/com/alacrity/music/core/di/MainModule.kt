package com.alacrity.music.core.di

import androidx.lifecycle.ViewModel
import com.alacrity.music.core.di.base.ViewModelKey
import com.alacrity.music.main.MainViewModel
import com.alacrity.music.repository.MusicRepository
import com.alacrity.music.repository.MusicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class MainModule {

    @IntoMap
    @Binds
    @Singleton
    @ViewModelKey(MainViewModel::class)
    abstract fun bindHomeViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun repository(impl: MusicRepositoryImpl): MusicRepository




}