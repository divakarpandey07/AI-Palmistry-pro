package com.example.palmistry.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/**
 * AnimatedPalmOverlay draws a pulsing hand outline over the camera preview
 * to guide the user to position their palm correctly.
 */
@Composable
fun AnimatedPalmOverlay(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "palm_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawPalmGuide(alpha)
    }
}

private fun DrawScope.drawPalmGuide(alpha: Float) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val r = size.width * 0.35f
    val color = Color(0xFF7C3AED).copy(alpha = alpha)
    val stroke = Stroke(width = 3.dp.toPx())

    // Draw palm oval guide
    drawOval(
        color = color,
        topLeft = Offset(cx - r, cy - r * 1.3f),
        size = androidx.compose.ui.geometry.Size(r * 2f, r * 2.6f),
        style = stroke
    )

    // Draw finger guides (5 lines)
    val fingerSpacing = (r * 1.6f) / 4f
    val startX = cx - r * 0.8f
    for (i in 0..4) {
        val fx = startX + i * fingerSpacing
        drawLine(
            color = color,
            start = Offset(fx, cy - r * 1.3f),
            end = Offset(fx, cy - r * 1.3f - 40.dp.toPx()),
            strokeWidth = 2.dp.toPx()
        )
    }

    // Crosshair center
    drawCircle(color = color, radius = 6.dp.toPx(), center = Offset(cx, cy))
}
