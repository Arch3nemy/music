package com.alacrity.music.core.di


import com.alacrity.music.utils.NetworkUtil
import com.alacrity.music.util.NetworkUtilImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NetworkModule {

    @Binds
    @Singleton
    fun bindNetworkUtil(impl: NetworkUtilImpl): NetworkUtil


}