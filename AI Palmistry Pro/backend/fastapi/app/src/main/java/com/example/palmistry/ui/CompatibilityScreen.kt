package com.example.palmistry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.components.ConfidenceGauge
import com.example.palmistry.ui.theme.*

@Composable
fun CompatibilityScreen(
    onNavigateBack: () -> Unit
) {
    var partner1Name by remember { mutableStateOf("") }
    var partner2Name by remember { mutableStateOf("") }
    var isMatchingDone by remember { mutableStateOf(false) }
    var matchScore by remember { mutableStateOf(88) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, Color(0xFF0F0C29), Color(0xFF1A0A2E))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .statusBarsPadding()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Text("⬅️", fontSize = 22.sp)
                }
                Text(
                    text = "💞 Hastrekha Couple Matching",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isMatchingDone) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardDark)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Partner 1 & Partner 2 Names",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = partner1Name,
                            onValueChange = { partner1Name = it },
                            label = { Text("Partner 1 Name", color = SoftWhite) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = partner2Name,
                            onValueChange = { partner2Name = it },
                            label = { Text("Partner 2 Name", color = SoftWhite) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (partner1Name.isNotBlank() && partner2Name.isNotBlank()) {
                                    matchScore = (78..96).random()
                                    isMatchingDone = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                        ) {
                            Text("🔮 Check Samudrik Compatibility", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepPurple)
                        }
                    }
                }
            } else {
                // Result Card
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ConfidenceGauge(confidenceScore = matchScore / 100f, size = 160.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "✨ $partner1Name & $partner2Name",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Text(
                        text = "Compatibility Score: $matchScore%",
                        fontSize = 16.sp,
                        color = SoftWhite
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardDark)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(text = "📌 Samudrik Shastra Milan Vishleshan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "• Hriday Rekha Harmony: Both partners have compatible emotional depth.\n• Guru Parvat Balance: Strong mutual respect and long-term marital prosperity.\n• Shakra Parvat Synergy: Positive domestic happiness & mutual understanding.",
                                fontSize = 14.sp,
                                color = SoftWhite,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { isMatchingDone = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                    ) {
                        Text("Check Another Couple", color = SoftWhite)
                    }
                }
            }
        }
    }
}
