package com.example.palmistry.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.GoldAccent
import com.example.palmistry.ui.NeonViolet
import com.example.palmistry.ui.SoftWhite

/**
 * Circular confidence gauge showing AI reading confidence (0-100%).
 * Animated arc fill on Canvas with gold percentage label in center.
 */
@Composable
fun ConfidenceGauge(
    confidence: Float,       // 0f to 1f
    size: Dp = 160.dp,
    modifier: Modifier = Modifier
) {
    val animatedSweep by animateFloatAsState(
        targetValue = confidence.coerceIn(0f, 1f) * 280f,  // 280° arc
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "gauge_sweep"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val inset = strokeWidth / 2
            val arcSize = Size(this.size.width - inset * 2, this.size.height - inset * 2)
            val startAngle = 130f   // starts bottom-left
            val topLeft = Offset(inset, inset)

            // Background track
            drawArc(
                color = Color.White.copy(alpha = 0.08f),
                startAngle = startAngle,
                sweepAngle = 280f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Filled arc
            drawArc(
                color = NeonViolet,
                startAngle = startAngle,
                sweepAngle = animatedSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(confidence * 100).toInt()}%",
                color = GoldAccent,
                fontSize = (size.value * 0.2f).sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Confidence",
                color = SoftWhite.copy(alpha = 0.6f),
                fontSize = (size.value * 0.09f).sp
            )
        }
    }
}
