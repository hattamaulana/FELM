package com.github.hattamaulana.moviecatalogue.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory
import com.github.hattamaulana.moviecatalogue.data.database.DatabaseHelper
import com.github.hattamaulana.moviecatalogue.data.database.FavoriteDao
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.widget.FavoriteWidgetProvider
import kotlinx.coroutines.runBlocking

class StackRemoteViewFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val TAG = this.javaClass.simpleName
    private val widgetItems = ArrayList<DataModel>()

    private lateinit var favoriteDao: FavoriteDao

    override fun onCreate() {
        favoriteDao = DatabaseHelper.openDb(context).favoriteDao()
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() = runBlocking {
        Log.d(TAG, "onDataSetChanged: OK;")

        val identityToken = Binder.clearCallingIdentity()
        val list = favoriteDao.getAllAsync()
        widgetItems.clear()
        widgetItems.addAll(list)

        Log.d(TAG, "onDataSetChanged: OK; ${widgetItems.size}")

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val intent = Intent()
            .apply { putExtras(bundleOf(FavoriteWidgetProvider.EXTRA_ITEM to position)) }
        val posterPath = widgetItems[position].posterPath
        val bitmap = Glide.with(context).asBitmap()
            .load("${MovieDbFactory.IMAGE_URI}/w780/$posterPath")
            .submit(780, 780)
            .get()

        return RemoteViews(context.packageName, R.layout.widget_favorite).apply {
            setImageViewBitmap(R.id.imageView, bitmap)
            setOnClickFillInIntent(R.id.imageView, intent)
        }
    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        widgetItems.clear()
    }
}