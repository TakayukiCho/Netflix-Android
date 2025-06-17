package com.codandotv.streamplayerapp

import android.app.Application
import com.codandotv.streamplayerapp.di.AppModule
import io.karte.android.KarteApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize KARTE SDK
        KarteApp.setup(this, BuildConfig.KARTE_APP_KEY)
        
        startKoin{
            androidContext(this@CustomApplication.applicationContext)
            modules(AppModule.list)
       }
    }
}