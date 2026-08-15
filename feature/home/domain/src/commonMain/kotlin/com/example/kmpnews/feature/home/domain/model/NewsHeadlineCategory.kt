package com.example.kmpnews.feature.home.domain.model

object NewsHeadlineCategory {
    const val BUSINESS = "business"
    const val ENTERTAINMENT = "entertainment"
    const val GENERAL = "general"
    const val HEALTH = "health"
    const val SCIENCE = "science"
    const val SPORTS = "sports"
    const val TECHNOLOGY = "technology"

    val all = listOf(
        GENERAL,
        BUSINESS,
        ENTERTAINMENT,
        HEALTH,
        SCIENCE,
        SPORTS,
        TECHNOLOGY,
    )
}
