package com.alacrity.music.di


import com.alacrity.music.NetworkUtil
import com.alacrity.music.Repository
import com.alacrity.music.RepositoryImpl
import com.alacrity.music.util.NetworkUtilImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NetworkModule {

    @Binds
    @Singleton
    fun bindNetworkUtil(impl: NetworkUtilImpl): NetworkUtil


 /*   @Binds
    @Singleton
    fun bindNewMessageReceivedUseCase(impl: NewMessageReceivedUseCaseImpl): NewMessageReceivedUseCase*/

    @Binds
    @Singleton
    fun bindRepository(impl: RepositoryImpl): Repository

}