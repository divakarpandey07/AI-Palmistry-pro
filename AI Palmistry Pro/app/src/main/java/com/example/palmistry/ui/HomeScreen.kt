package com.example.palmistry.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onStartScan: () -> Unit,
    onOpenKundli: () -> Unit,
    onOpenTarot: () -> Unit,
    onOpenNumerology: () -> Unit,
    onViewHistory: () -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF07040E), Color(0xFF130C29), Color(0xFF090615))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "AI PALMISTRY PRO",
                    color = Color(0xFFF3C654),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    "Vedic Palmistry & Astro Intelligence",
                    color = Color(0xFFCBD5E1).copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = onViewHistory,
                modifier = Modifier
                    .background(Color(0xFF1E143B), CircleShape)
                    .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.Filled.History, contentDescription = "History", tint = Color(0xFFF3C654))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Daily Insight Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1135)),
                border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stars,
                        contentDescription = null,
                        tint = Color(0xFFF3C654),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            " Aaj Ka Grah Yog",
                            color = Color(0xFFF3C654),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Chandrama aur Guru ki sthiti se aaj ka din naye karyon aur aatm-vishwas ke liye shubh hai.",
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Hero Banner - Palm Scan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartScan() },
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E195E)),
                border = BorderStroke(1.5.dp, Color(0xFFF3C654))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF4C1D95), Color(0xFF2E195E), Color(0xFF6B21A8))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF3C654).copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Color(0xFFF3C654))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CameraAlt,
                                    contentDescription = null,
                                    tint = Color(0xFFF3C654),
                                    modifier = Modifier.padding(10.dp).size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    "Palmistry AI Scanner",
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Instant High-Precision Palm Line Analysis",
                                    color = Color(0xFFCBD5E1).copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            "Apne haath ki photu scan karein aur Heart, Head, Life va Fate lines ka deep analysis praapt karein.",
                            color = Color(0xFFE2E8F0),
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onStartScan,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                        ) {
                            Text(
                                "SCAN MY PALM NOW",
                                color = Color(0xFF090614),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Astro & Esoteric Modules",
                color = Color(0xFFF3C654),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Grid Feature Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                FeatureTile(
                    title = "Janma Kundli",
                    subtitle = "Vedic Birth Chart",
                    icon = Icons.Filled.AutoAwesome,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenKundli
                )
                FeatureTile(
                    title = "Tarot Reading",
                    subtitle = "3-Card Insight",
                    icon = Icons.Filled.Style,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenTarot
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                FeatureTile(
                    title = "Numerology",
                    subtitle = "Life Path & Destiny",
                    icon = Icons.Filled.Calculate,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenNumerology
                )
                FeatureTile(
                    title = "Past Readings",
                    subtitle = "Saved History",
                    icon = Icons.Filled.History,
                    modifier = Modifier.weight(1f),
                    onClick = onViewHistory
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun FeatureTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(135.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181033)),
        border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF6B21A8).copy(alpha = 0.3f),
                border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFF3C654),
                    modifier = Modifier.padding(8.dp).size(20.dp)
                )
            }

            Column {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, color = Color(0xFFCBD5E1).copy(alpha = 0.6f), fontSize = 11.sp)
            }
        }
    }
}
