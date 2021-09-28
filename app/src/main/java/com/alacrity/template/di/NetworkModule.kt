package com.alacrity.template.di


import com.alacrity.template.NetworkUtil
import com.alacrity.template.Repository
import com.alacrity.template.RepositoryImpl
import com.alacrity.template.util.NetworkUtilImpl
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