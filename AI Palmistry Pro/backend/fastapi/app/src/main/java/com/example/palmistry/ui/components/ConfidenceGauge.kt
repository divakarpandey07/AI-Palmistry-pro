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
import com.example.palmistry.ui.theme.*

/**
 * Circular confidence gauge showing AI reading confidence (0-100%).
 * Animated arc fill on Canvas with gold percentage label in center.
 */
@Composable
fun ConfidenceGauge(
    confidenceScore: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    strokeWidth: Dp = 12.dp
) {
    val targetSweepAngle = (confidenceScore.coerceIn(0f, 1f)) * 260f

    val animatedSweepAngle by animateFloatAsState(
        targetValue = targetSweepAngle,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "gauge_sweep"
    )

    val percentage = (confidenceScore * 100).toInt()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(size.toPx() - strokePx, size.toPx() - strokePx)
            val topLeft = Offset(strokePx / 2, strokePx / 2)
            val startAngle = 140f

            // Background arc (dark track)
            drawArc(
                color = Color(0xFF2D1B69),
                startAngle = startAngle,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Active animated progress arc (NeonViolet)
            drawArc(
                color = NeonViolet,
                startAngle = startAngle,
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percentage%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
            Text(
                text = "Confidence",
                fontSize = 11.sp,
                color = SoftWhite.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
