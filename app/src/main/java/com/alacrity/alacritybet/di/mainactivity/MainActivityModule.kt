package com.alacrity.alacritybet.di.mainactivity

import androidx.lifecycle.ViewModel
import com.alacrity.alacritybet.di.base.ViewModelKey
import com.alacrity.alacritybet.ui.main.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

    @IntoMap
    @Binds
    @MainActivityScope
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindViewModel(viewModel: MainActivityViewModel): ViewModel

}