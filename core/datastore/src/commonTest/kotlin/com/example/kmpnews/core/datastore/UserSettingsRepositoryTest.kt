package com.example.kmpnews.core.datastore

import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class UserSettingsRepositoryTest {
    @Test
    fun settings_roundTrip() = runTest {
        val repository = UserSettingsRepositoryImpl(MapSettings())

        repository.setThemeMode(ThemeMode.DARK)
        repository.setLanguage("tr")
        repository.setApiKey("secret-key")

        assertEquals(ThemeMode.DARK, repository.themeMode.first())
        assertEquals("tr", repository.language.first())
        assertEquals("secret-key", repository.apiKey.first())
    }
}
