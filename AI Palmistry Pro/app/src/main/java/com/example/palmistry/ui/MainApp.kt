package com.example.palmistry.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.palmistry.ui.viewmodel.ReadingUiState

// Premium dark color palette
val DeepPurple = Color(0xFF1A0A2E)
val NeonViolet = Color(0xFF7C3AED)
val GoldAccent  = Color(0xFFF59E0B)
val SoftWhite   = Color(0xFFF1F5F9)
val CardDark    = Color(0xFF2D1B69)

@Composable
fun MainApp(
    uiState: ReadingUiState,
    onPalmCaptured: (String) -> Unit,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, Color(0xFF0F0C29), Color(0xFF302B63))
                )
            )
    ) {
        when (uiState) {
            is ReadingUiState.Idle -> {
                CameraScreen(onPalmMetadataReady = onPalmCaptured)
                

            }

            is ReadingUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = NeonViolet, strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "🔮 AI aapka haath padh raha hai...",
                        color = SoftWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            is ReadingUiState.Success -> {
                ReadingResultScreen(reading = uiState.reading, onReset = onReset)
            }

            is ReadingUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("❌ Error: ${uiState.message}", color = Color.Red, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onReset, colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)) {
                        Text("Dobara Koshish Karein", color = SoftWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingResultScreen(reading: String, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text("🔮 Aapka Haath Padha Gaya!", color = GoldAccent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark)
        ) {
            Text(
                text = reading,
                modifier = Modifier.padding(20.dp),
                color = SoftWhite,
                fontSize = 16.sp,
                lineHeight = 26.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Naya Reading Lein", fontSize = 16.sp, color = SoftWhite, fontWeight = FontWeight.Bold)
        }
    }
}
