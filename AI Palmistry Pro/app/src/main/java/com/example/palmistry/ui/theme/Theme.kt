package com.example.palmistry.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------
// Color Palette
// ---------------------------------------------------------------
val DeepPurple   = Color(0xFF1A0A2E)
val NeonViolet   = Color(0xFF7C3AED)
val GoldAccent   = Color(0xFFF59E0B)
val SoftWhite    = Color(0xFFF1F5F9)
val CardDark     = Color(0xFF2D1B69)
val DarkSurface  = Color(0xFF0F0C29)
val ErrorRed     = Color(0xFFEF4444)

// ---------------------------------------------------------------
// Dark Color Scheme
// ---------------------------------------------------------------
private val DarkColorScheme = darkColorScheme(
    primary        = NeonViolet,
    onPrimary      = SoftWhite,
    secondary      = GoldAccent,
    onSecondary    = DeepPurple,
    background     = DeepPurple,
    onBackground   = SoftWhite,
    surface        = CardDark,
    onSurface      = SoftWhite,
    error          = ErrorRed,
    onError        = SoftWhite
)

// ---------------------------------------------------------------
// Typography (system font fallback – replace with Google Font in production)
// ---------------------------------------------------------------
private val PalmistryTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)

// ---------------------------------------------------------------
// App Theme Composable
// ---------------------------------------------------------------
@Composable
fun PalmistryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography   = PalmistryTypography,
        content      = content
    )
}
