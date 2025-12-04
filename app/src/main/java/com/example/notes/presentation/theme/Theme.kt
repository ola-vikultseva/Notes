package com.example.notes.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF519AFD),
    secondary = Color(0xFF282828),
    background = Color(0xFF171717),
    surface = Color(0xFF282828),
    outline = Color(0xFF282828),

    onPrimary = Color(0xFFDEDEDE),
    onSecondary = Color(0xFFDEDEDE),
    onBackground = Color(0xFFDEDEDE),
    onSurface = Color(0xFFDEDEDE),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF519AFD),
    secondary = Color(0xFFE4F0FD),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    outline = Color(0xFFC7C7C7),

    onPrimary = Color(0xFFFFFBFE),
    onSecondary = Color(0xFF519AFD),
    onBackground = Color(0xFF7E7E7E),
    onSurface = Color(0xFF7E7E7E),
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}