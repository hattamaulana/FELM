package com.github.hattamaulana.moviecatalogue.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.hattamaulana.moviecatalogue.R
import kotlinx.coroutines.runBlocking

internal val CHANNEL_ID = "CHANNEL_0"
internal val CHANNEL_NAME = "APP_MOVIE_DB_CHANNEL"

fun Context.sendNotification(
    idNotification: Int,
    title: Int,
    message: Int,
    intent: Intent? = null
) = runBlocking {
    val titleNotification = resources.getStringArray(title)[idNotification]
    val messageNotification = resources.getStringArray(message)[idNotification]
    val pendingIntent = PendingIntent.getActivity(
        this@sendNotification, 0, intent, 0)
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    val notificationBuilder = NotificationCompat.Builder(
        this@sendNotification, idNotification.toString())
        .setContentTitle(titleNotification)
        .setContentText(messageNotification)
        .setSmallIcon(R.drawable.ic_baseline_notifications)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "$CHANNEL_ID$idNotification"
        val channel = NotificationChannel(channelId, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationBuilder.setChannelId(channelId)
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(idNotification, notificationBuilder.build())
}