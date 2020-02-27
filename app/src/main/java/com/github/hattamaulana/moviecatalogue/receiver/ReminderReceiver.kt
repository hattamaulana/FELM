package com.github.hattamaulana.moviecatalogue.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.MainActivity
import com.github.hattamaulana.moviecatalogue.utils.sendNotification
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val notifId = if (type == TYPE_DAILY_REMAINDER) ID_DAILY_REMAINDER else ID_NEW_RELEASE
        val title = R.array.title_notification
        val message = R.array.message_notification
        val intentAction = when (type) {
            TYPE_DAILY_REMAINDER -> Intent(context, MainActivity::class.java)
            TYPE_NEW_RELEASE -> Intent(context, MainActivity::class.java)
            else -> null
        }

        context.sendNotification(notifId, title, message, intentAction)
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
    private fun setOn(context: Context, time: String, type: String, data: Parcelable? = null) {
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

    companion object {
        const val TYPE_DAILY_REMAINDER = "TYPE_DAILY_REMAINDER"
        const val TYPE_NEW_RELEASE = "TYPE_NEW_RELEASE"
        const val EXTRA_TYPE = "EXTRA_TYPE"

        private const val ID_DAILY_REMAINDER = 0
        private const val ID_NEW_RELEASE = 1
    }
}
