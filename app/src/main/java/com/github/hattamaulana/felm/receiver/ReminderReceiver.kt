package com.github.hattamaulana.felm.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.data.remote.MovieDbRepository
import com.github.hattamaulana.felm.ui.MainActivity
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import com.github.hattamaulana.felm.ui.newrelease.NewReleaseActivity
import com.github.hattamaulana.felm.utils.sendNotification
import com.github.hattamaulana.felm.utils.sendStackNotification
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)

        when (type) {
            TYPE_DAILY_REMAINDER -> {
                val title = context.resources.getString(R.string.notif_title_early_morning)
                val message = context.resources.getString(R.string.notif_message_early_morning)
                val pendingIntent = Intent(context, MainActivity::class.java)
                context.sendNotification(ID_DAILY_REMAINDER, title, message, pendingIntent)
            }

            TYPE_NEW_RELEASE -> getNewRelease(context)
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
    private fun getNewRelease(context: Context) {
        val repo = MovieDbRepository(context)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        repo.newRelease(TYPE_MOVIE, dateFormat.format(Date())) { list, _ ->
            if (list.isNotEmpty()) {
                val messages = ArrayList<String>()
                list.forEach { data -> messages.add(data.title ?: "") }

                if (messages.size > 3) {
                    val pendingIntent = Intent(context, NewReleaseActivity::class.java)
                    context.sendStackNotification(messages, pendingIntent)
                } else {
                    messages.forEachIndexed { i, msg ->
                        val pendingIntent = Intent(context, DetailActivity::class.java)
                            .apply {
                                putExtra(DetailActivity.EXTRA_ID, list[i].id)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }

                        val title = "$msg ${context.resources.getString(R.string.notif_title_new_release)}"
                        val message = context.resources.getString(R.string.notif_message_new_release)
                        context.sendNotification(i, title, message, pendingIntent)
                    }
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
