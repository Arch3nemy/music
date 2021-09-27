package com.alacrity.alacritybet.di.base

import androidx.lifecycle.ViewModelProvider
import com.alacrity.alacritybet.di.base.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(impl: DaggerViewModelFactory): ViewModelProvider.Factory

}