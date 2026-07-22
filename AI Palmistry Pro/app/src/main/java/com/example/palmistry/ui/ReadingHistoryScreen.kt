package com.example.palmistry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.data.model.ReadingEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Displays a scrollable history of past palm readings from the local encrypted Room DB.
 */
@Composable
fun ReadingHistoryScreen(
    readings: List<ReadingEntity>,
    onDelete: (Int) -> Unit
) {
    if (readings.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Abhi koi reading nahi hai.\nPehle apna haath scan karein!",
                color = SoftWhite.copy(alpha = 0.6f),
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(readings, key = { it.id }) { reading ->
            ReadingHistoryCard(reading = reading, onDelete = { onDelete(reading.id) })
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
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(date, color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = onDelete) {
                    Text("Delete", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reading.readingResult,
                color = SoftWhite,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { reading.confidenceScore },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = NeonViolet,
                trackColor = SoftWhite.copy(alpha = 0.1f)
            )
            Text(
                "Confidence: ${(reading.confidenceScore * 100).toInt()}%",
                color = SoftWhite.copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }
    }
}
