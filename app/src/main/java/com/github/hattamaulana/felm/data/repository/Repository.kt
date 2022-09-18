package com.github.hattamaulana.felm.data.repository

import android.content.Context
import com.github.hattamaulana.android.core.common.BaseRepository

abstract class Repository(context: Context): BaseRepository(context) {
    override fun catchException(directHandling: Boolean) {
    }

    override suspend fun refreshToken() {
    }
}