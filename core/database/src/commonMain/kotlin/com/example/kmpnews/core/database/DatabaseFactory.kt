package com.example.kmpnews.core.database

import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteDriver
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

expect class DatabaseFactory {
    fun createDatabase(): NewsDatabase
}

fun RoomDatabase.Builder<NewsDatabase>.buildNewsDatabase(
    driver: SQLiteDriver,
    queryCoroutineContext: CoroutineContext = EmptyCoroutineContext,
): NewsDatabase =
    setDriver(driver)
        .setQueryCoroutineContext(queryCoroutineContext)
        .build()
