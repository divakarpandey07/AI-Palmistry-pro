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
import com.example.palmistry.ui.theme.*
import com.example.palmistry.ui.viewmodel.ReadingUiState

@Composable
fun MainApp(
    uiState: ReadingUiState,
    onPalmCaptured: (String) -> Unit,
    onReset: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf("Bilingual") }

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
                CameraScreen(
                    selectedLanguage = selectedLanguage,
                    onPalmMetadataReady = onPalmCaptured,
                    onNavigateHome = onReset
                )
            }
            is ReadingUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = GoldAccent)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "🔮 AI aapki rekhaon ko padh raha hai...",
                        color = SoftWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            is ReadingUiState.Success -> {
                ReadingResultScreen(
                    readingResult = uiState.reading,
                    onBackToHome = onReset
                )
            }
            is ReadingUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌ Error: ${uiState.message}",
                        color = Color.Red,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = onReset,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
