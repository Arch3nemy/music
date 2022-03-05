package com.alacrity.music.core.di

import com.alacrity.music.core.App
import com.alacrity.music.core.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, MainModule::class, UseCaseModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(app: App)

}