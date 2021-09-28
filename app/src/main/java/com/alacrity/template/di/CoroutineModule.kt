package com.alacrity.template.di

import com.alacrity.template.dispatchers.CoroutineDispatchers
import com.alacrity.template.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface CoroutineModule {

    @Binds
    @Singleton
    fun bindDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers

}