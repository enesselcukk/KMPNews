package com.example.kmpnews.core.domain.repository

import com.example.kmpnews.core.model.Article

interface ArticleRepository {
    fun getArticles(): List<Article>

    fun getArticle(id: String): Article?
}
