package com.example.kmpnews.core.database

import androidx.room3.Room
import kotlinx.coroutines.Dispatchers
import java.io.File

actual class DatabaseFactory {
    actual fun createDatabase(): NewsDatabase {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "kmpnews.db")
        return Room.databaseBuilder<NewsDatabase>(
            name = dbFile.absolutePath,
        ).buildNewsDatabase(Dispatchers.IO)
    }
}
