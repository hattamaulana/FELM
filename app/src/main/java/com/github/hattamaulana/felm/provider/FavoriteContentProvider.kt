package com.github.hattamaulana.felm.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.github.hattamaulana.felm.data.database.DatabaseHelper
import com.github.hattamaulana.felm.data.model.DATA_TABLE_NAME
import com.github.hattamaulana.felm.data.model.GENRE_TABLE_NAME

class FavoriteContentProvider : ContentProvider() {

    private var helper = DatabaseHelper

    override fun onCreate(): Boolean {
        helper.openHelper(context as Context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            FAVORITE -> helper.getAll()
            FAVORITE_ID -> helper.getDataById(uri.lastPathSegment.toString())
            GENRE_ID -> helper.getGenreById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
        helper.remove(selection as String, selectionArgs as Array<String>)


    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun getType(uri: Uri): String? = null

    companion object {
        private const val AUTHORITY = "com.github.hattamaulana.felm.app"
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private const val GENRE_ID = 3

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            // content://com.github.hattamaulana.felm.app/felm.favorites
            addURI(AUTHORITY, DATA_TABLE_NAME, FAVORITE)

            // content://com.github.hattamaulana.felm.app/felm.favorites/favorite_id
            addURI(AUTHORITY, "$DATA_TABLE_NAME/#", FAVORITE_ID)

            // content://com.github.hattamaulana.felm.app/genres/favorite_id
            addURI(AUTHORITY, "$GENRE_TABLE_NAME/#", GENRE_ID)
        }
    }
}
