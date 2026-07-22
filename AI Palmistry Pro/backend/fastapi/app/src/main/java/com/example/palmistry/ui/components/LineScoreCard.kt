package com.example.palmistry.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.CardDark
import com.example.palmistry.ui.NeonViolet
import com.example.palmistry.ui.SoftWhite

/**
 * LineScoreCard shows individual palm line scores as animated progress bars.
 * lineScores: map of line name -> score (0f to 1f)
 */
@Composable
fun LineScoreCard(lineScores: Map<String, Float>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Palm Line Analysis", color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            lineScores.forEach { (name, score) ->
                LineScoreRow(name = name, score = score)
            }
        }
    }
}

@Composable
private fun LineScoreRow(name: String, score: Float) {
    val animatedScore by animateFloatAsState(
        targetValue = score.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "score_$name"
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, color = SoftWhite.copy(alpha = 0.85f), fontSize = 13.sp)
            Text("${(animatedScore * 100).toInt()}%", color = NeonViolet, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
        androidx.compose.material3.LinearProgressIndicator(
            progress = { animatedScore },
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = NeonViolet,
            trackColor = SoftWhite.copy(alpha = 0.1f)
        )
    }
}
