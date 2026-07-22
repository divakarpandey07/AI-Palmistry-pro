package com.example.palmistry.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.theme.*

/**
 * LineScoreCard shows individual palm line scores as animated progress bars.
 * lineScores: map of line name -> score (0f to 1f)
 */
@Composable
fun LineScoreCard(lineScores: Map<String, Float>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Palm Line Analysis",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SoftWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            lineScores.forEach { (lineName, rawScore) ->
                val targetProgress = rawScore.coerceIn(0f, 1f)
                val animatedProgress by animateFloatAsState(
                    targetValue = targetProgress,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    label = "line_score_$lineName"
                )

                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = lineName, fontSize = 13.sp, color = SoftWhite)
                        Text(
                            text = "${(targetProgress * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonViolet
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = NeonViolet,
                        trackColor = Color(0xFF1A0A2E)
                    )
                }
            }
        }
    }
}
