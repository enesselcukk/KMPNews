package com.example.kmpnews.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SdhOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFF4E2600),
    secondary = SdhNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5E3F3),
    onSecondaryContainer = SdhNavyDark,
    tertiary = SdhNavyDark,
    onTertiary = Color.White,
    background = SdhBackground,
    onBackground = SdhOnBackground,
    surface = Color.White,
    onSurface = SdhOnBackground,
    surfaceVariant = Color(0xFFE8EAED),
    onSurfaceVariant = SdhOnSurfaceVariant,
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2D2D2D),
    inverseOnSurface = SdhOnMediaOverlay,
)

private val DarkColorScheme = darkColorScheme(
    primary = SdhOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C2D00),
    onPrimaryContainer = Color(0xFFFFDCC3),
    secondary = SdhNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1B2838),
    onSecondaryContainer = Color(0xFFD5E3F3),
    tertiary = SdhNavyDark,
    onTertiary = Color.White,
    background = SdhDarkBackground,
    onBackground = SdhDarkOnBackground,
    surface = SdhDarkSurface,
    onSurface = SdhDarkOnBackground,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = SdhDarkOnSurfaceVariant,
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE8EAED),
    inverseOnSurface = Color(0xFF1A1A1A),
)

@Composable
fun KmpNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KmpNewsTypography,
        content = content,
    )
}
