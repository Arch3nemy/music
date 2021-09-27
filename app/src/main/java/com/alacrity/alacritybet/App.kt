package com.alacrity.alacritybet

import android.app.Application
import com.alacrity.alacritybet.di.AppComponent
import com.alacrity.alacritybet.di.AppModule
import com.alacrity.alacritybet.di.DaggerAppComponent

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
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
            .apply { inject(this@App) }
    }
}