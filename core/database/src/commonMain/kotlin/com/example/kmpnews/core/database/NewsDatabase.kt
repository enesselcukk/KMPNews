package com.example.kmpnews.core.database

import androidx.room3.AutoMigration
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.kmpnews.core.database.dao.ArticleDao
import com.example.kmpnews.core.database.dao.HomeHeadlineDao
import com.example.kmpnews.core.database.model.ArticleEntity
import com.example.kmpnews.core.database.model.HomeHeadlineEntity

@Database(
    entities = [
        ArticleEntity::class,
        HomeHeadlineEntity::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@ConstructedBy(NewsDatabaseConstructor::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    abstract fun homeHeadlineDao(): HomeHeadlineDao
}

@Suppress("KotlinNoActualForExpect")
expect object NewsDatabaseConstructor : RoomDatabaseConstructor<NewsDatabase> {
    override fun initialize(): NewsDatabase
}
