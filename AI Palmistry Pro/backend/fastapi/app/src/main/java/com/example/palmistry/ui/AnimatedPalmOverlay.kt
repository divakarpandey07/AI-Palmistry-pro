package com.example.palmistry.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.palmistry.ui.theme.GoldAccent
import com.example.palmistry.ui.theme.NeonViolet

@Composable
fun AnimatedPalmOverlay(
    isHighlighterMode: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "palm_scan")
    
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_y"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Outer Guide Oval
        val center = Offset(width / 2f, height / 2f)
        val rx = width * 0.38f
        val ry = height * 0.30f

        drawOval(
            color = NeonViolet.copy(alpha = pulseAlpha),
            topLeft = Offset(center.x - rx, center.y - ry),
            size = androidx.compose.ui.geometry.Size(rx * 2f, ry * 2f),
            style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f)))
        )

        // Scanning Laser Beam Line
        val currentScanY = height * scanLineY
        drawLine(
            color = GoldAccent.copy(alpha = 0.85f),
            start = Offset(center.x - rx * 0.9f, currentScanY),
            end = Offset(center.x + rx * 0.9f, currentScanY),
            strokeWidth = 5f
        )

        // Visual Palm Line Highlighter Overlay
        if (isHighlighterMode) {
            // 💚 Jeevan Rekha (Life Line - Green)
            val lifeLinePath = Path().apply {
                moveTo(width * 0.38f, height * 0.48f)
                cubicTo(
                    width * 0.34f, height * 0.58f,
                    width * 0.38f, height * 0.68f,
                    width * 0.44f, height * 0.74f
                )
            }
            drawPath(
                path = lifeLinePath,
                color = Color(0xFF00E676).copy(alpha = 0.9f),
                style = Stroke(width = 4f)
            )

            // ❤️ Hriday Rekha (Heart Line - Red/Pink)
            val heartLinePath = Path().apply {
                moveTo(width * 0.68f, height * 0.44f)
                cubicTo(
                    width * 0.55f, height * 0.45f,
                    width * 0.42f, height * 0.48f,
                    width * 0.36f, height * 0.44f
                )
            }
            drawPath(
                path = heartLinePath,
                color = Color(0xFFFF1744).copy(alpha = 0.9f),
                style = Stroke(width = 4f)
            )

            // 💙 Mastishk Rekha (Head Line - Blue)
            val headLinePath = Path().apply {
                moveTo(width * 0.36f, height * 0.47f)
                cubicTo(
                    width * 0.48f, height * 0.50f,
                    width * 0.58f, height * 0.54f,
                    width * 0.66f, height * 0.57f
                )
            }
            drawPath(
                path = headLinePath,
                color = Color(0xFF29B6F6).copy(alpha = 0.9f),
                style = Stroke(width = 4f)
            )

            // 💛 Bhagya Rekha (Fate Line - Gold)
            val fateLinePath = Path().apply {
                moveTo(width * 0.50f, height * 0.72f)
                lineTo(width * 0.51f, height * 0.46f)
            }
            drawPath(
                path = fateLinePath,
                color = GoldAccent.copy(alpha = 0.95f),
                style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f)))
            )
        }
    }
}
