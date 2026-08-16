package com.example.kmpnews.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.kmpnews.core.database.model.HomeHeadlineEntity

@Dao
interface HomeHeadlineDao {
    @Query("SELECT * FROM home_headlines WHERE category = :category ORDER BY sortOrder ASC")
    suspend fun getByCategory(category: String): List<HomeHeadlineEntity>

    @Query("SELECT * FROM home_headlines WHERE articleUrl = :url LIMIT 1")
    suspend fun findByUrl(url: String): HomeHeadlineEntity?

    @Query("DELETE FROM home_headlines WHERE category = :category")
    suspend fun deleteByCategory(category: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<HomeHeadlineEntity>)

    @Transaction
    suspend fun replaceCategoryArticles(category: String, articles: List<HomeHeadlineEntity>) {
        deleteByCategory(category)
        insertAll(articles)
    }
}
