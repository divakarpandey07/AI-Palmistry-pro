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
    onOpenHistory: () -> Unit
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
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AI Palmistry Pro",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldAccent,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Vedic Samudrik Shastra & AI Palmistry Analysis",
                fontSize = 14.sp,
                color = SoftWhite.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Language Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.9f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🌐 Choose Response Language / Bhasha Chunein",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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

            Spacer(modifier = Modifier.height(24.dp))

            // Main CTA Button - Start Camera Scan
            Button(
                onClick = onStartScan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "📸", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Scan My Palm / Haath Scan Karein",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // History Button
            OutlinedButton(
                onClick = onOpenHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonViolet)
            ) {
                Text(
                    text = "📜 View Past Readings / Purani Reading",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftWhite
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Features Grid
            Text(
                text = "✨ App Key Features",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SoftWhite,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                icon = "✋",
                title = "3 Classical Books Knowledge",
                desc = "Samudrik Shastra, Vrihad Hastrekha Shastra, Samudrik Hastrekha Vigyan data base."
            )
            FeatureItem(
                icon = "🔮",
                title = "Marriage & Career Predictions",
                desc = "Shadi timing, Job/Career timing, Dhan yog & Life outcomes."
            )
            FeatureItem(
                icon = "🔒",
                title = "100% Encrypted & Private",
                desc = "AES-256 military grade payload security for 100% privacy."
            )

            Spacer(modifier = Modifier.height(32.dp))
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

@Composable
private fun FeatureItem(icon: String, title: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Text(text = desc, fontSize = 12.sp, color = SoftWhite.copy(alpha = 0.8f))
            }
        }
    }
}
