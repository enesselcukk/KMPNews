package com.example.kmpnews.shared.di

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.ArticleRepository
import com.example.kmpnews.core.model.sampleArticles

class DefaultArticleRepository : ArticleRepository {
    override fun getArticles(): List<Article> = sampleArticles

    override fun getArticle(id: String): Article? = sampleArticles.find { it.id == id }
}
