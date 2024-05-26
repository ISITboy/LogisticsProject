package com.example.diploma

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("e9277176-fdb5-46fd-8e26-3d1c933703a3")
    }
}