package com.alacrity.music.di

import com.alacrity.music.dispatchers.CoroutineDispatchers
import com.alacrity.music.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface CoroutineModule {

    @Binds
    @Singleton
    fun bindDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers

}