package com.github.hattamaulana.moviecatalogue

import android.app.Application
import android.content.Context
import com.androidnetworking.AndroidNetworking

class App : Application() {

    class SharedPref(context: Context) {

        private val sharedPref =
            context.getSharedPreferences(App::class.java.name, Context.MODE_PRIVATE)

        var firstRun: Boolean
            get() = sharedPref.getBoolean(FIRST_RUN, true)
            set(value) {
                val editor = sharedPref.edit()
                editor.putBoolean(FIRST_RUN, value)
                editor.apply()
            }

        companion object {
            const val FIRST_RUN = "FIRST_RUN"
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
    }
}