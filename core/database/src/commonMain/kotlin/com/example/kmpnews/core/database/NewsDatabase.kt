package com.example.kmpnews.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.kmpnews.core.database.dao.ArticleDao
import com.example.kmpnews.core.database.model.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1,
)
@ConstructedBy(NewsDatabaseConstructor::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}

@Suppress("KotlinNoActualForExpect")
expect object NewsDatabaseConstructor : RoomDatabaseConstructor<NewsDatabase> {
    override fun initialize(): NewsDatabase
}
