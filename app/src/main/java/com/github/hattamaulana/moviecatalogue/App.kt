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

        var notificationEarlyMorning: Boolean
            get() = sharedPref.getBoolean(NOTIFICATION_EARLY_MORNING, true)
            set(value) {
                val editor = sharedPref.edit()
                editor.putBoolean(NOTIFICATION_EARLY_MORNING, value)
                editor.apply()
            }

        var notificationNewRelease: Boolean
            get() = sharedPref.getBoolean(NOTIFICATION_NEW_RELEASE, true)
            set(value) {
                val editor = sharedPref.edit()
                editor.putBoolean(NOTIFICATION_NEW_RELEASE, value)
                editor.apply()
            }

        companion object {
            const val FIRST_RUN = "FIRST_RUN"
            const val NOTIFICATION_EARLY_MORNING = "NOTIFICATION_EARLY_MORNING"
            const val NOTIFICATION_NEW_RELEASE = "NOTIFICATION_NEW_RELEASE"
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
    }
}