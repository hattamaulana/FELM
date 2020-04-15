package com.github.hattamaulana.felm.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.hattamaulana.felm.R

internal val CHANNEL_ID = "CHANNEL_0"
internal val CHANNEL_NAME = "APP_MOVIE_DB_CHANNEL"

fun Context.sendNotification(notifId: Int, title: String, message: String, intent: Intent) {
    val pendingIntent = PendingIntent.getActivity(
        this, 0, intent, 0)
    val notificationBuilder = NotificationCompat.Builder(
        this, CHANNEL_ID + notifId.toString())
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(notifId, notificationBuilder)
}

fun Context.sendStackNotification(messages: List<String>, intent: Intent) {
    intent.apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP }

    val GROUP_KEY = "com.github.hattamaulana.felm.app"
    val NOTIF_REQ_CODE = 200
    val notifId = messages.size
    val pendingIntent = PendingIntent.getActivity(
        this, NOTIF_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val stack = NotificationCompat.InboxStyle()
        .setBigContentTitle("${messages.size} Movies ${resources.getString(R.string.notif_title_new_release)}")
        .setSummaryText(resources.getString(R.string.app_name))

    messages.forEach { message -> stack.addLine(message) }

    val notifBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(notifId.toString() + resources.getString(R.string.notif_title_new_release))
        .setContentText(resources.getString(R.string.notif_message_new_release))
        .setSmallIcon(R.drawable.ic_launcher)
        .setGroup(GROUP_KEY)
        .setGroupSummary(true)
        .setContentIntent(pendingIntent)
        .setStyle(stack)
        .setAutoCancel(true)

    notify(notifId, notifBuilder)
}

private fun Context.notify(notifId: Int, notificationBuilder: NotificationCompat.Builder) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    val channelId = CHANNEL_ID + notifId.toString()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationBuilder.setChannelId(channelId)
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(notifId, notificationBuilder.build())
}