package com.example.palmistry.ui

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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
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

@Composable
fun NumerologyScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var lifePathResult by remember { mutableStateOf<NumerologyResult?>(null) }

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
                "Numerology & Life Path Calculator",
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
            if (lifePathResult == null) {
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
                            imageVector = Icons.Filled.Calculate,
                            contentDescription = null,
                            tint = Color(0xFFF3C654),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Ank Jyotish (Numerology)",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Apne janam tithi aur naam ke anko se apne Moolank, Bhagyank aur vyaktitva ki jankari praapt karein.",
                            color = Color(0xFFE2E8F0).copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                    label = { Text("Janam Tithi (DDMMYYYY)", color = Color(0xFFCBD5E1)) },
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

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        if (birthDate.isBlank()) return@Button
                        lifePathResult = calculateNumerology(name, birthDate)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8)),
                    border = BorderStroke(1.dp, Color(0xFFF3C654))
                ) {
                    Text("Ank Ganana Karein", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                NumerologyResultView(result = lifePathResult!!, onReset = { lifePathResult = null })
            }
        }
    }
}

data class NumerologyResult(
    val lifePathNumber: Int,
    val destinyNumber: Int,
    val title: String,
    val description: String,
    val luckyColor: String,
    val luckyDay: String
)

private fun calculateNumerology(name: String, dob: String): NumerologyResult {
    val digits = dob.filter { it.isDigit() }.map { it.toString().toInt() }
    var sum = digits.sum()
    while (sum > 9 && sum != 11 && sum != 22) {
        sum = sum.toString().map { it.toString().toInt() }.sum()
    }

    val meanings = mapOf(
        1 to ("Neta (Leader)" to "Aap ek swantantra aur nayeeyan laane wale neta hain. Aap mein netritva kshamta praakritik hai."),
        2 to ("Kutnitijna (Diplomat)" to "Aap shant, vicharsheel aur sahyogathmak hain. Aap sambandhon ko sanjoye rakhte hain."),
        3 to ("Rachanatmak (Creator)" to "Aap prabhavshali bolne wale aur rachanatmak vyakti hain. Kala aur abhivyakti aapki shakti hai."),
        4 to ("Nirmata (Builder)" to "Aap vyavaharik, kadi mehnat karne wale aur imaandar hain. Aap neev mazboot banate hain."),
        5 to ("Yatri (Explorer)" to "Aap swatantrata-priya aur utsahipurna hain. Aapko naye anubhav aur parivartan pasand hain."),
        6 to ("Sanrakshak (Protector)" to "Aap prem, sewa aur parivaar ke prati samarpit hain. Aap dusron ka dhyan rakhte hain."),
        7 to ("Chintak (Thinker)" to "Aap gahan gyan aur adhyatmikta ke khoji hain. Aap har baat ki tah tak jaate hain."),
        8 to ("Uddyami (Executive)" to "Aap samriddhi aur safalta praapt karne wale aatmanirbhar vyakti hain."),
        9 to ("Manavta-wadi (Humanitarian)" to "Aap uddaar, dayalu aur samaj ke liye kaam karne wale vyakti hain.")
    )

    val detail = meanings[sum] ?: ("Gyani (Seeker)" to "Aapke paas adhyatmik gyan aur vilakshan kshamta hai.")

    return NumerologyResult(
        lifePathNumber = sum,
        destinyNumber = (sum + 3) % 9 + 1,
        title = detail.first,
        description = detail.second,
        luckyColor = if (sum % 2 == 0) "Peela (Yellow) & Safed (White)" else "Keshariya (Orange) & Laal (Red)",
        luckyDay = if (sum % 2 == 0) "Somvar (Monday)" else "Ravivar (Sunday)"
    )
}

@Composable
private fun NumerologyResultView(result: NumerologyResult, onReset: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E143B)),
        border = BorderStroke(1.dp, Color(0xFFF3C654).copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF6B21A8), CircleShape)
                    .border(2.dp, Color(0xFFF3C654), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${result.lifePathNumber}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFF3C654)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Moolank ${result.lifePathNumber}: ${result.title}", color = Color(0xFFF3C654), fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(14.dp))

            Text(result.description, color = Color(0xFFE2E8F0), fontSize = 14.sp, lineHeight = 22.sp, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(20.dp))

            Divider(color = Color(0xFFD4AF37).copy(alpha = 0.3f))

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Shubh Rang (Lucky Color):", color = Color(0xFFCBD5E1), fontSize = 13.sp)
                Text(result.luckyColor, color = Color(0xFFF3C654), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Shubh Din (Lucky Day):", color = Color(0xFFCBD5E1), fontSize = 13.sp)
                Text(result.luckyDay, color = Color(0xFFF3C654), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onReset,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8))
    ) {
        Text("Naya Ank Dekhein", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
