package com.example.kmpnews.core.model

interface ArticleRepository {
    fun getArticles(): List<Article>

    fun getArticle(id: String): Article?
}
