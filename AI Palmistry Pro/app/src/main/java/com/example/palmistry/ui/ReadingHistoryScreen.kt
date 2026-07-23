package com.example.palmistry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

// Local color constants (same as MainApp palette)
private val _HistorySoftWhite = Color(0xFFF1F5F9)
private val _HistoryGoldAccent = Color(0xFFF59E0B)
private val _HistoryCardDark = Color(0xFF2D1B69)
private val _HistoryNeonViolet = Color(0xFF7C3AED)

/**
 * Premium history screen for past palm readings.
 */
@Composable
fun ReadingHistoryScreen(
    readings: List<ReadingEntity>,
    onDelete: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D0621), Color(0xFF1A0A2E), Color(0xFF0F0C29))
                )
            )
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                "📜 Reading History",
                color = _HistoryGoldAccent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (readings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🤚", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Abhi koi reading nahi hai.",
                        color = _HistorySoftWhite.copy(alpha = 0.7f),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Pehle apna haath scan karein!",
                        color = _HistorySoftWhite.copy(alpha = 0.45f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(readings, key = { it.id }) { reading ->
                ReadingHistoryCard(reading = reading, onDelete = { onDelete(reading.id) })
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
        colors = CardDefaults.cardColors(containerColor = _HistoryCardDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(date, color = _HistoryGoldAccent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = onDelete) {
                    Text("🗑 Delete", color = Color(0xFFEF4444), fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reading.readingResult,
                color = _HistorySoftWhite,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { reading.confidenceScore },
                modifier = Modifier.fillMaxWidth().height(5.dp),
                color = _HistoryNeonViolet,
                trackColor = _HistorySoftWhite.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Confidence: ${(reading.confidenceScore * 100).toInt()}%",
                color = _HistorySoftWhite.copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }
    }
}
