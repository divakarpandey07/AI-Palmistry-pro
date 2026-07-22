package com.example.palmistry.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DeepPurple = Color(0xFF1A0A2E)
val NeonViolet = Color(0xFF7C3AED)
val GoldAccent = Color(0xFFF59E0B)
val SoftWhite  = Color(0xFFF1F5F9)
val CardDark   = Color(0xFF2D1B69)

private val DarkColorScheme = darkColorScheme(
    primary = NeonViolet,
    secondary = GoldAccent,
    background = DeepPurple,
    surface = CardDark,
    onPrimary = SoftWhite,
    onBackground = SoftWhite,
    onSurface = SoftWhite
)

@Composable
fun PalmistryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
