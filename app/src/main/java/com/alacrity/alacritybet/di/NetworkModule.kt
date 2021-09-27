package com.alacrity.alacritybet.di


import com.alacrity.alacritybet.NetworkUtil
import com.alacrity.alacritybet.Repository
import com.alacrity.alacritybet.RepositoryImpl
import com.alacrity.alacritybet.util.NetworkUtilImpl
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