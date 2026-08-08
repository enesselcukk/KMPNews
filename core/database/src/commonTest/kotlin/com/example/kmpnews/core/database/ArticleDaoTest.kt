package com.example.kmpnews.core.database

import androidx.room3.Room
import com.example.kmpnews.core.database.model.ArticleEntity
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class ArticleDaoTest {
    @Test
    fun insertAndReadArticle() = runTest {
        val database = Room.inMemoryDatabaseBuilder<NewsDatabase>()
            .buildNewsDatabase(coroutineContext)

        val article = Article(
            id = "1",
            title = "KMP News",
            description = "Description",
            content = null,
            url = "https://example.com",
            imageUrl = null,
            publishedAt = "2026-08-08T09:00:00Z",
            source = Source(id = "news", name = "News"),
            author = "Author",
        )

        database.articleDao().insertAll(listOf(ArticleEntity.fromDomain(article)))

        val stored = database.articleDao().getArticleById("1").first()
        assertEquals(article, stored?.toDomain())

        database.close()
    }
}
