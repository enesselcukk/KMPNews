package com.example.kmpnews.core.database

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

expect class DatabaseFactory {
    fun createDatabase(): NewsDatabase
}

fun RoomDatabase.Builder<NewsDatabase>.buildNewsDatabase(
    queryCoroutineContext: CoroutineContext = EmptyCoroutineContext,
): NewsDatabase =
    setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(queryCoroutineContext)
        .build()
