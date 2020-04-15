package com.github.hattamaulana.felm.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.service.StackWidgetService
import com.github.hattamaulana.felm.ui.detail.DetailActivity

class FavoriteWidgetProvider : AppWidgetProvider() {

    private val TAG = this.javaClass.simpleName

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate: OK;")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        when (intent?.action) {
            EXTRA_ACTION -> {
                context?.startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_ID, intent.getIntExtra(EXTRA_ITEM, 0))
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }

            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val widget = ComponentName(context?.packageName as String,
                    FavoriteWidgetProvider::class.java.name)
                AppWidgetManager.getInstance(context).apply {
                    val widgetIds = getAppWidgetIds(widget)
                    notifyAppWidgetViewDataChanged(widgetIds, R.id.stack_view)
                }
            }
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
            action = EXTRA_ACTION
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
        private const val EXTRA_ACTION = "com.github.hattamaulana.felm.app.TOAST_ACTION"
        const val EXTRA_ITEM = "com.github.hattamaulana.felm.app.EXTRA_ITEM"
    }
}