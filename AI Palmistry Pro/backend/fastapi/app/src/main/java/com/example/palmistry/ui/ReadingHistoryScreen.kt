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
import com.example.palmistry.ui.theme.*
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DeepPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Past Palm Readings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SoftWhite,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (readings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No readings saved yet.\nScan your palm to get started!",
                        color = SoftWhite.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(readings, key = { it.id }) { reading ->
                        ReadingItemCard(reading = reading, onDelete = { onDelete(reading.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadingItemCard(
    reading: ReadingEntity,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(reading.timestamp) { dateFormat.format(Date(reading.timestamp)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = GoldAccent
                )
                IconButton(onClick = onDelete) {
                    Text("🗑️", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reading.readingResult,
                fontSize = 14.sp,
                color = SoftWhite,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text("Life: ${(reading.lifeLineScore * 100).toInt()}%", color = SoftWhite) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = NeonViolet.copy(alpha = 0.3f))
                )
                AssistChip(
                    onClick = {},
                    label = { Text("Confidence: ${(reading.confidenceScore * 100).toInt()}%", color = SoftWhite) }
                )
            }
        }
    }
}
