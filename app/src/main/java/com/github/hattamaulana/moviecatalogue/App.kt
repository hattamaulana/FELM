package com.github.hattamaulana.moviecatalogue

import android.app.Application
import com.androidnetworking.AndroidNetworking

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
    }

}