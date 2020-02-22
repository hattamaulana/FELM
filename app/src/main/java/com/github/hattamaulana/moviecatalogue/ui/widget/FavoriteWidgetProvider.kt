package com.github.hattamaulana.moviecatalogue.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.service.StackWidgetService

class FavoriteWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.takeIf { intent.action == TOAST_ACTION  }?.let {
            val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
            // TODO Do Something
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StackWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = this.toUri(Intent.URI_INTENT_SCHEME).toUri()
        }

        val actionIntent = Intent(context, FavoriteWidgetProvider::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            action = TOAST_ACTION
            data = this.toUri(Intent.URI_INTENT_SCHEME).toUri()
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val views = RemoteViews(context.packageName, R.layout.favorite_widget)
            .apply {
                setRemoteAdapter(R.id.stack_view, intent)
                setEmptyView(R.id.stack_view, R.id.empty_view)
                setPendingIntentTemplate(R.id.stack_view, pendingIntent)
            }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        private const val TOAST_ACTION = "com.github.hattamaulana.moviecatalogue.TOAST_ACTION"
        const val EXTRA_ITEM = "com.github.hattamaulana.moviecatalogue.EXTRA_ITEM"
    }
}