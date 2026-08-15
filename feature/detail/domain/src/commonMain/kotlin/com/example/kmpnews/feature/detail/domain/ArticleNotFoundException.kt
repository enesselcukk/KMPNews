package com.example.kmpnews.feature.detail.domain

class ArticleNotFoundException(
    val articleUrl: String,
) : Exception("Article not found for url: $articleUrl")
