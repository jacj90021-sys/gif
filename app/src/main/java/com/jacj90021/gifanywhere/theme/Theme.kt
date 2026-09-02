package com.jacj90021.gifanywhere.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BgYellow,
    onPrimary = InkBlack,
    secondary = Color(0xFFE6BE00),
    onSecondary = InkBlack,
    background = BgYellow,
    onBackground = InkBlack,
    surface = CardWhite,
    onSurface = InkBlack,
    surfaceVariant = CardWhite,
    onSurfaceVariant = InkMuted,
    outline = InkBlack,
    error = Color(0xFFFF3B3B),
)

@Composable
fun GifAnywhereTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
