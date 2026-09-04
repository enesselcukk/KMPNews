package com.example.kmpnews.core.datastore

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSettingsRepositoryImpl(
    private val settings: Settings = Settings(),
) : UserSettingsRepository {
    private val themeModeState = MutableStateFlow(readThemeMode())
    private val languageState = MutableStateFlow(readLanguage())
    private val apiKeyState = MutableStateFlow(readApiKey())

    override val themeMode: Flow<ThemeMode> = themeModeState.asStateFlow()
    override val language: Flow<String> = languageState.asStateFlow()
    override val apiKey: Flow<String> = apiKeyState.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        settings[SettingsKeys.THEME_MODE] = mode.name
        themeModeState.value = mode
    }

    override suspend fun setLanguage(language: String) {
        settings[SettingsKeys.LANGUAGE] = language
        languageState.value = language
    }

    override suspend fun setApiKey(apiKey: String) {
        settings[SettingsKeys.API_KEY] = apiKey
        apiKeyState.value = apiKey
    }

    private fun readThemeMode(): ThemeMode =
        themeModeFromStorage(settings.getStringOrNull(SettingsKeys.THEME_MODE))

    private fun readLanguage(): String =
        settings.getStringOrNull(SettingsKeys.LANGUAGE) ?: "en"

    private fun readApiKey(): String =
        settings.getStringOrNull(SettingsKeys.API_KEY) ?: ""
}
