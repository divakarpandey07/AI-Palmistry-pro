package com.example.palmistry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.palmistry.ui.theme.*

@Composable
fun HomeScreen(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onStartScan: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenCompatibility: () -> Unit
) {
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // App Branding Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        Brush.radialGradient(listOf(NeonViolet, CardDark)),
                        shape = CircleShape
                    )
                    .border(2.dp, GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🤚", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "AI Palmistry Pro",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldAccent,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Vedic Samudrik Shastra & AI Astrological Reading",
                fontSize = 13.sp,
                color = SoftWhite.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Shubh Muhurat Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.9f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌞", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Aaj Ka Shubh Muhurat & Astro Tip",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                        Text(
                            text = "Abhijit Muhurat: 11:58 AM - 12:48 PM | Guru Parvat Sthiti: Shubh",
                            fontSize = 11.sp,
                            color = SoftWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Language Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.9f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌐 Choose Response Language / Bhasha Chunein",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageOptionChip(
                            label = "🇮🇳 Hindi",
                            isSelected = selectedLanguage == "Hindi",
                            onClick = { onLanguageSelected("Hindi") },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageOptionChip(
                            label = "🇬🇧 English",
                            isSelected = selectedLanguage == "English",
                            onClick = { onLanguageSelected("English") },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageOptionChip(
                            label = "🌗 Both",
                            isSelected = selectedLanguage == "Bilingual",
                            onClick = { onLanguageSelected("Bilingual") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main CTA Button - Start Camera Scan
            Button(
                onClick = onStartScan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "📸", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Scan My Palm / Haath Scan Karein",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Partner Compatibility Button
                Button(
                    onClick = onOpenCompatibility,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                ) {
                    Text("💞 Couple Matching", fontSize = 13.sp, color = SoftWhite, fontWeight = FontWeight.Bold)
                }

                // History Button
                OutlinedButton(
                    onClick = onOpenHistory,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonViolet)
                ) {
                    Text("📜 Past History", fontSize = 13.sp, color = SoftWhite, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gemstone & Rudraksha Recommendation Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💎 Gemstone & Rudraksha Recommendation",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Guru Parvat Strong: Yellow Sapphire (Pukhraj) & 5-Mukhi Rudraksha\n• Surya Rekha Support: Ruby (Manikya) for Career & Prestige",
                        fontSize = 12.sp,
                        color = SoftWhite.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LanguageOptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                if (isSelected) GoldAccent else DeepPurple.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (isSelected) GoldAccent else SoftWhite.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) DeepPurple else SoftWhite
        )
    }
}
