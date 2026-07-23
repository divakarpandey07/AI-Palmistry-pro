package com.example.palmistry.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun KundliScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var birthTime by remember { mutableStateOf("") }
    var birthPlace by remember { mutableStateOf("") }
    var generatedReport by remember { mutableStateOf<KundliReport?>(null) }
    var isCalculating by remember { mutableStateOf(false) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF090614), Color(0xFF140D2B), Color(0xFF0A0718))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .statusBarsPadding()
    ) {
        // Top Bar
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
                "Vedic Kundli & Astro Chart",
                color = Color(0xFFF3C654),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (generatedReport == null) {
                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E143B).copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFF3C654),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Janma Kundli Analysis",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Apni janam tithi aur samay darj karke apna Lagna, Rashi, aur Grah Dasha report praapt karein.",
                            color = Color(0xFFE2E8F0).copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Input Form
                Text(
                    "Birth Details",
                    color = Color(0xFFD4AF37),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Pura Naam (Full Name)", color = Color(0xFFCBD5E1)) },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFFD4AF37)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF3C654),
                        unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Janam Tithi (DD/MM/YYYY)", color = Color(0xFFCBD5E1)) },
                    leadingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color(0xFFD4AF37)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF3C654),
                        unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = birthTime,
                    onValueChange = { birthTime = it },
                    label = { Text("Janam Samay (HH:MM AM/PM)", color = Color(0xFFCBD5E1)) },
                    leadingIcon = { Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFFD4AF37)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF3C654),
                        unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = birthPlace,
                    onValueChange = { birthPlace = it },
                    label = { Text("Janam Sthan (City, Country)", color = Color(0xFFCBD5E1)) },
                    leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFD4AF37)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF3C654),
                        unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || birthDate.isBlank()) return@Button
                        isCalculating = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8)),
                    border = BorderStroke(1.dp, Color(0xFFF3C654))
                ) {
                    if (isCalculating) {
                        CircularProgressIndicator(color = Color(0xFFF3C654), modifier = Modifier.size(24.dp))
                    } else {
                        Text("Kundli Calculate Karein", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Simulate Kundli Calculation delay
                LaunchedEffect(isCalculating) {
                    if (isCalculating) {
                        kotlinx.coroutines.delay(1800)
                        generatedReport = calculateKundli(name, birthDate, birthPlace)
                        isCalculating = false
                    }
                }
            } else {
                // Show Kundli Result
                KundliResultView(report = generatedReport!!, onReset = { generatedReport = null })
            }
        }
    }
}

data class KundliReport(
    val name: String,
    val rashi: String,
    val nakshatra: String,
    val lagna: String,
    val element: String,
    val planetDasha: String,
    val summary: String,
    val remedies: String
)

private fun calculateKundli(name: String, dob: String, place: String): KundliReport {
    val rashis = listOf("Mesha (Aries)", "Vrishabha (Taurus)", "Mithuna (Gemini)", "Karka (Cancer)", "Simha (Leo)", "Kanya (Virgo)", "Tula (Libra)", "Vrishchika (Scorpio)", "Dhanu (Sagittarius)", "Makara (Capricorn)", "Kumbha (Aquarius)", "Meena (Pisces)")
    val nakshatras = listOf("Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni")
    val elements = listOf("Agni (Fire)", "Prithvi (Earth)", "Vayu (Air)", "Jal (Water)")

    val hash = Math.abs(name.hashCode() + dob.hashCode())
    val selectedRashi = rashis[hash % rashis.size]
    val selectedNakshatra = nakshatras[hash % nakshatras.size]
    val selectedElement = elements[hash % elements.size]

    return KundliReport(
        name = name,
        rashi = selectedRashi,
        nakshatra = selectedNakshatra,
        lagna = "Simha (Leo Ascendant)",
        element = selectedElement,
        planetDasha = "Brihaspati (Jupiter) Mahadasha",
        summary = "Aapki Janma Kundli ke anusar aapka Chandrama $selectedRashi mein sthit hai. Aap ek dridh nishchayi aur prabhavshali vyaktitva ke swami hain. Guru Mahadasha ke prabhav se agle 3 saal mein aapko vyapaar aur career mein unnati praapt hogi.",
        remedies = "Pratyek Guruvar ko haldi milaye pani se Surya dev ko arghya dein aur Brihaspati Mantra ka jaap karein."
    )
}

@Composable
private fun KundliResultView(report: KundliReport, onReset: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E143B)),
        border = BorderStroke(1.dp, Color(0xFFF3C654).copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Astro Report for ${report.name}",
                color = Color(0xFFF3C654),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MetricTile("Rashi", report.rashi)
                MetricTile("Nakshatra", report.nakshatra)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MetricTile("Element", report.element)
                MetricTile("Dasha", report.planetDasha)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(color = Color(0xFFD4AF37).copy(alpha = 0.3f))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Kundli Vishleshan", color = Color(0xFFF3C654), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(report.summary, color = Color(0xFFE2E8F0), fontSize = 14.sp, lineHeight = 22.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Jyotish Upay (Remedies)", color = Color(0xFFF3C654), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(report.remedies, color = Color(0xFFCBD5E1), fontSize = 13.sp, lineHeight = 20.sp)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onReset,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8))
    ) {
        Text("Nayi Kundli Dekhein", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RowScope.MetricTile(title: String, value: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
            .background(Color(0xFF120B2E), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(title, color = Color(0xFFD4AF37), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
