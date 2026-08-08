package com.example.kmpnews.core.database

import androidx.room3.Room
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSTemporaryDirectory

actual class DatabaseFactory {
    actual fun createDatabase(): NewsDatabase {
        val dbPath = NSTemporaryDirectory() + "kmpnews.db"
        return Room.databaseBuilder<NewsDatabase>(
            name = dbPath,
        ).buildNewsDatabase(Dispatchers.Default)
    }
}
