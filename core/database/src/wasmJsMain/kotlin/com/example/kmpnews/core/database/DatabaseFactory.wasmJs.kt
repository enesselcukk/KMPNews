package com.example.kmpnews.core.database

import androidx.room3.Room
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Worker

actual class DatabaseFactory {
    actual fun createDatabase(): NewsDatabase =
        Room.databaseBuilder<NewsDatabase>(
            name = "kmpnews.db",
        ).buildNewsDatabase(
            driver = WebWorkerSQLiteDriver(createSqliteWorker()),
            queryCoroutineContext = Dispatchers.Default,
        )
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun createSqliteWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")
