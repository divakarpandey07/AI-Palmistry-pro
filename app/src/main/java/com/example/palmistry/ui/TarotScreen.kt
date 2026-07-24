package com.example.palmistry.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TarotCardData(
    val title: String,
    val period: String,
    val cardName: String,
    val symbol: String,
    val meaning: String,
    var isFlipped: Boolean = false
)

@Composable
fun TarotScreen(onBack: () -> Unit) {
    var cards by remember {
        mutableStateOf(
            listOf(
                TarotCardData("Past", "Bhootkaal", "The High Priestess", "☽", "Aapka aatm-gyan aur pratyaksh gyan hamesha se shaktishali raha hai. Ateet ke anubhavon se aapne gahra gyan praapt kiya hai."),
                TarotCardData("Present", "Vartaman", "The Wheel of Fortune", "☸", "Vartaman mein jeevan mein bada parivartan aa raha hai. Bhagya aapke paksh mein ghoom raha hai. Naye avsar samne aayenge."),
                TarotCardData("Future", "Bhavishya", "The Sun", "☀", "Aane wala samay aasha, safalta aur prakash se bhara hai. Aapke sabhi prayas safal honge aur sukh-samriddhi praapt hogi.")
            )
        )
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF090614), Color(0xFF1B0E38), Color(0xFF0A0718))
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
                "3-Card Mystical Tarot Reading",
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
            Text(
                "Card Touch Karein Aur Apna Bhagya Dekhein",
                color = Color(0xFFE2E8F0),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                cards.forEachIndexed { index, card ->
                    TarotCardTile(
                        card = card,
                        onClick = {
                            cards = cards.toMutableList().also {
                                it[index] = card.copy(isFlipped = !card.isFlipped)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Interpretations List
            cards.forEach { card ->
                if (card.isFlipped) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E143B)),
                        border = BorderStroke(1.dp, Color(0xFFF3C654).copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${card.title} (${card.period})",
                                    color = Color(0xFFF3C654),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    card.cardName,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                card.meaning,
                                color = Color(0xFFCBD5E1),
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            if (cards.any { it.isFlipped }) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        cards = cards.map { it.copy(isFlipped = false) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8))
                ) {
                    Icon(Icons.Filled.RotateRight, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Shuffle & Reset Cards", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TarotCardTile(card: TarotCardData, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped) 180f else 0f,
        animationSpec = tween(600),
        label = "card_flip"
    )

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rotation > 90f) Color(0xFF2D1B69) else Color(0xFF140D2B)
        ),
        border = BorderStroke(1.5.dp, Color(0xFFD4AF37))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Back side of card
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFF3C654),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        card.title,
                        color = Color(0xFFF3C654),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Front side of card (Flipped)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(card.symbol, fontSize = 28.sp, color = Color(0xFFF3C654))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        card.cardName,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}
