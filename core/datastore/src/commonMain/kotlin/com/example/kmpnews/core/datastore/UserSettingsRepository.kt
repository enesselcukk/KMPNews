package com.example.kmpnews.core.datastore

import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val themeMode: Flow<ThemeMode>
    val language: Flow<String>
    val apiKey: Flow<String>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setLanguage(language: String)
    suspend fun setApiKey(apiKey: String)
}