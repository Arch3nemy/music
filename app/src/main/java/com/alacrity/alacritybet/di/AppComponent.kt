package com.alacrity.alacritybet.di

import com.alacrity.alacritybet.App
import com.alacrity.alacritybet.di.mainactivity.MainActivityModule
import com.alacrity.alacritybet.di.mainactivity.MainActivityScope
import com.alacrity.alacritybet.ui.main.MainActivity
import dagger.Component

@MainActivityScope
@Component(modules = [AppModule::class, MainActivityModule::class, CoroutineModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(app: App)

}