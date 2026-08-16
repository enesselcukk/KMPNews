package com.example.kmpnews.core.database.model

import androidx.room3.Entity

@Entity(
    tableName = "home_headlines",
    primaryKeys = ["category", "articleUrl"],
)
data class HomeHeadlineEntity(
    val category: String,
    val articleUrl: String,
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val sortOrder: Int,
)
