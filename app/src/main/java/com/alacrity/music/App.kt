package com.alacrity.music

import android.app.Application
import com.alacrity.music.di.AppComponent
import com.alacrity.music.di.AppModule
import com.alacrity.music.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    companion object {

        lateinit var appComponent: AppComponent

        /*var detailComponent: DetailComponent? = null
            get() {
                if (field == null) field = appComponent.provideDetailComponent()
                return field
            }


        fun clearDetailComponent() {
            detailComponent = null
        }*/
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
            .apply { inject(this@App) }
    }
}