package com.example.palmistry.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.data.model.ReadingEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReadingHistoryScreen(
    readings: List<ReadingEntity>,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF07040E), Color(0xFF130C29), Color(0xFF090615))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFD4AF37))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Reading History & Saved Reports",
                color = Color(0xFFF3C654),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (readings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = Color(0xFFD4AF37).copy(alpha = 0.5f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Koi Purani Reading Nahi Hai",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Pehle apna haath scan karein ya Kundli calculate karein!",
                        color = Color(0xFFCBD5E1).copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(readings, key = { it.id }) { reading ->
                    ReadingHistoryCard(reading = reading, onDelete = { onDelete(reading.id) })
                }
            }
        }
    }
}

@Composable
fun ReadingHistoryCard(reading: ReadingEntity, onDelete: () -> Unit) {
    val date = remember(reading.timestamp) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(reading.timestamp))
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181033)),
        border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(date, color = Color(0xFFF3C654), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reading.readingResult,
                color = Color(0xFFE2E8F0),
                fontSize = 13.sp,
                lineHeight = 21.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = reading.confidenceScore,
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = Color(0xFF6B21A8),
                trackColor = Color(0xFFE2E8F0).copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Confidence Metric: ${(reading.confidenceScore * 100).toInt()}%",
                color = Color(0xFFCBD5E1).copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }
    }
}
