package com.github.hattamaulana.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.github.hattamaulana.moviecatalogue.data.database.AppDbProvider
import com.github.hattamaulana.moviecatalogue.data.database.AppDbProvider.AUTHORITY
import com.github.hattamaulana.moviecatalogue.data.database.AppDbProvider.TABLE_NAME
import com.github.hattamaulana.moviecatalogue.data.database.FavoriteDao

class FavoriteContentProvider : ContentProvider() {

    private lateinit var favoriteDao: FavoriteDao

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun onCreate(): Boolean {
        favoriteDao = AppDbProvider.getDb(context as Context).favoriteDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            FAVORITE -> favoriteDao.getAll()
            FAVORITE_ID -> favoriteDao.findById(uri.lastPathSegment?.toInt() as Int)
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            // content://com.github.hattamaulana.moviecatalogue/note
            addURI(AUTHORITY, TABLE_NAME, FAVORITE)

            // content://com.github.hattamaulana.moviecatalogue/note/note_id
            addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }
}
