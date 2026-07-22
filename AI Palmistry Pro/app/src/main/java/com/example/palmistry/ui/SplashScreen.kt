package com.example.palmistry.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val DeepPurple  = Color(0xFF1A0A2E)
val NeonViolet  = Color(0xFF7C3AED)
val GoldAccent  = Color(0xFFF59E0B)
val SoftWhite   = Color(0xFFF1F5F9)
val CardDark    = Color(0xFF2D1B69)

@Composable
fun SplashScreen(onSplashDone: () -> Unit) {

    // Navigate after 2.5 seconds
    LaunchedEffect(Unit) {
        delay(2500)
        onSplashDone()
    }

    // Shimmer animation for title
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by shimmerTransition.animateFloat(
        initialValue = -300f,
        targetValue  = 1000f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "shimmer_offset"
    )

    // Subtitle fade-in
    val subtitleAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 800),
        label = "subtitle_alpha"
    )

    // Pulsing palm icon scale
    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val iconScale by pulseTransition.animateFloat(
        initialValue = 0.95f,
        targetValue  = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, Color(0xFF302B63), Color(0xFF0F0C29))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Palm icon (text-based for now; replace with LottieAnimation or VectorPainter)
            Text(
                text = "🤚",
                fontSize = (80 * iconScale).sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Shimmer title
            val shimmerBrush = Brush.linearGradient(
                colors = listOf(GoldAccent, Color.White, GoldAccent),
                start = Offset(shimmerOffset, 0f),
                end = Offset(shimmerOffset + 300f, 300f)
            )

            Text(
                text = "AI Palmistry Pro",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                style = androidx.compose.ui.text.TextStyle(brush = shimmerBrush),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Powered by AI & Ancient Wisdom",
                fontSize = 15.sp,
                color = SoftWhite.copy(alpha = 0.75f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(subtitleAlpha)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Animated dots loader
            val dotAlpha1 by pulseTransition.animateFloat(
                initialValue = 0.2f, targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
                label = "d1"
            )
            val dotAlpha2 by pulseTransition.animateFloat(
                initialValue = 0.2f, targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse),
                label = "d2"
            )
            val dotAlpha3 by pulseTransition.animateFloat(
                initialValue = 0.2f, targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse),
                label = "d3"
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(dotAlpha1, dotAlpha2, dotAlpha3).forEach { a ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(NeonViolet.copy(alpha = a), shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }
        }
    }
}
