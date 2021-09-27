package com.alacrity.alacritybet.di

import com.alacrity.alacritybet.dispatchers.CoroutineDispatchers
import com.alacrity.alacritybet.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface CoroutineModule {

    @Binds
    @Singleton
    fun bindDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers

}