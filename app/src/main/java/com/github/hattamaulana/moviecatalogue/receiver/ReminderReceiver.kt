package com.github.hattamaulana.moviecatalogue.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.ui.MainActivity
import com.github.hattamaulana.moviecatalogue.ui.newrelease.NewReleaseActivity
import com.github.hattamaulana.moviecatalogue.utils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class ReminderReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val notifId = if (type == TYPE_DAILY_REMAINDER) ID_DAILY_REMAINDER else ID_NEW_RELEASE
        val title = R.array.title_notification
        val message = R.array.message_notification

        when (type) {
            TYPE_DAILY_REMAINDER -> {
                val pendingIntent = Intent(context, MainActivity::class.java)
                context.sendNotification(notifId, title, message, pendingIntent)
            }

            TYPE_NEW_RELEASE -> getNewRelease(context, notifId, title, message)
        }
    }

    fun setDailyMorning(context: Context) {
        setOn(context, "07:00", TYPE_DAILY_REMAINDER)
    }

    fun setNewRelease(context: Context) {
        setOn(context, "08:00", TYPE_NEW_RELEASE)
    }

    /** Set Off Remainder */
    fun setOff(context: Context, type: String) {
        val requestId = if (type == TYPE_DAILY_REMAINDER) ID_DAILY_REMAINDER else ID_NEW_RELEASE
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent
            .getBroadcast(context, requestId, intent, 0)
            .apply { cancel() }

        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
            cancel(pendingIntent)
        }
    }

    /** Set On Reminder daily and new release info */
    private fun setOn(context: Context, time: String, type: String) {
        val id = if (type == TYPE_DAILY_REMAINDER) ID_DAILY_REMAINDER else ID_NEW_RELEASE
        val timeArray = time.split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
        }
        val intent = Intent(context, ReminderReceiver::class.java)
            .apply { putExtra(EXTRA_TYPE, type) }
        val pendingIntent = PendingIntent
            .getBroadcast(context, id, intent, 0)

        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
            setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getNewRelease(context: Context, notifId: Int, title: Int, message: Int) {
        val repo = MovieDbRepository(context)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        launch {
            repo.newRelease(TYPE_MOVIE, dateFormat.format(Date())) { list, _ ->
                if (list.isNotEmpty()) {
                    list.forEach { data -> }
                    val pendingIntent = Intent(context, NewReleaseActivity::class.java)
                    context.sendNotification(notifId, title, message, pendingIntent)
                }
            }
        }
    }

    companion object {
        const val TYPE_DAILY_REMAINDER = "TYPE_DAILY_REMAINDER"
        const val TYPE_NEW_RELEASE = "TYPE_NEW_RELEASE"
        const val EXTRA_TYPE = "EXTRA_TYPE"

        private const val ID_DAILY_REMAINDER = 0
        const val ID_NEW_RELEASE = 1
    }
}
