package com.alacrity.template.di

import com.alacrity.template.App
import com.alacrity.template.ui.main.MainActivity
import dagger.Component

@Component(modules = [AppModule::class, CoroutineModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(app: App)

}