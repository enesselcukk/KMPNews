package com.example.kmpnews.core.database

import android.content.Context
import androidx.room3.Room
import kotlinx.coroutines.Dispatchers

actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun createDatabase(): NewsDatabase =
        Room.databaseBuilder<NewsDatabase>(
            context = context.applicationContext,
            name = context.applicationContext.getDatabasePath("kmpnews.db").absolutePath,
        ).buildNewsDatabase(Dispatchers.IO)
}
