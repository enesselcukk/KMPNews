package com.example.kmpnews.feature.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String,
)
