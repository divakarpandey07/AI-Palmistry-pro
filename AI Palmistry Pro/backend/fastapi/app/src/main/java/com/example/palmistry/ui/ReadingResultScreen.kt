package com.example.palmistry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.theme.*

@Composable
fun ReadingResultScreen(
    readingResult: String,
    onBackToHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, Color(0xFF0F0C29), Color(0xFF302B63))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨ Hastrekha Vishleshan",
                color = GoldAccent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = readingResult,
                    modifier = Modifier.padding(16.dp),
                    color = SoftWhite,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBackToHome,
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
            ) {
                Text("Wapas Jaaein", color = SoftWhite)
            }
        }
    }
}
