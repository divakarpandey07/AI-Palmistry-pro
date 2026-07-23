package com.example.palmistry.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.palmistry.ui.viewmodel.HistoryViewModel
import com.example.palmistry.ui.viewmodel.ReadingUiState

// ── Premium dark color palette (exported for other files) ──────────────────
val DeepPurple = Color(0xFF1A0A2E)
val NeonViolet = Color(0xFF7C3AED)
val GoldAccent  = Color(0xFFF59E0B)
val SoftWhite   = Color(0xFFF1F5F9)
val CardDark    = Color(0xFF2D1B69)

// ── Screen state ────────────────────────────────────────────────────────────
enum class AppScreen { HOME, CAMERA, RESULT, HISTORY, ERROR }

@Composable
fun MainApp(
    uiState: ReadingUiState,
    onPalmCaptured: (String) -> Unit,
    onReset: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var lastReading by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // React to ViewModel state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is ReadingUiState.Success -> {
                lastReading = uiState.reading
                currentScreen = AppScreen.RESULT
            }
            is ReadingUiState.Error -> {
                errorMessage = uiState.message
                currentScreen = AppScreen.ERROR
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0621), Color(0xFF1A0A2E), Color(0xFF0F0C29))
                )
            )
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(tween(400)) togetherWith fadeOut(tween(300))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> HomeScreen(
                    onStartScan = { currentScreen = AppScreen.CAMERA },
                    onViewHistory = { currentScreen = AppScreen.HISTORY }
                )

                AppScreen.CAMERA -> {
                    if (uiState is ReadingUiState.Loading) {
                        PremiumLoadingScreen()
                    } else {
                        CameraScreen(
                            onPalmMetadataReady = { palmJson ->
                                onPalmCaptured(palmJson)
                            }
                        )
                    }
                }

                AppScreen.RESULT -> ReadingResultScreen(
                    reading = lastReading,
                    onReset = {
                        onReset()
                        currentScreen = AppScreen.HOME
                    }
                )

                AppScreen.ERROR -> FullScreenErrorView(
                    message = errorMessage,
                    onRetry = {
                        onReset()
                        currentScreen = AppScreen.CAMERA
                    },
                    onHome = {
                        onReset()
                        currentScreen = AppScreen.HOME
                    }
                )

                AppScreen.HISTORY -> {
                    val historyViewModel: HistoryViewModel = hiltViewModel()
                    val readings by historyViewModel.readings.collectAsState()
                    ReadingHistoryScreen(
                        readings = readings,
                        onDelete = { id -> historyViewModel.deleteReading(id) }
                    )
                }
            }
        }
    }
}

// ── Premium loading screen ──────────────────────────────────────────────────
@Composable
fun PremiumLoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    val dotAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "d1"
    )
    val dotAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse), label = "d2"
    )
    val dotAlpha3 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse), label = "d3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0D0621), Color(0xFF1A0A2E)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🔮", fontSize = (64 * pulseScale).sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "AI Haath Padh Raha Hai...",
                color = SoftWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(
                    shadow = Shadow(NeonViolet.copy(alpha = 0.8f), Offset(0f, 4f), 16f)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Aapke haath ki rekhaon ka analysis ho raha hai",
                color = SoftWhite.copy(alpha = 0.5f), fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(dotAlpha1, dotAlpha2, dotAlpha3).forEach { a ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(NeonViolet.copy(alpha = a), CircleShape)
                    )
                }
            }
        }
    }
}

// ── Full-screen premium error view ──────────────────────────────────────────
@Composable
fun FullScreenErrorView(
    message: String,
    onRetry: () -> Unit,
    onHome: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "error_anim")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -4f, targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(300, easing = LinearEasing), RepeatMode.Reverse),
        label = "shake"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1A0A2E), Color(0xFF2D0A0A), Color(0xFF1A0A2E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFEF4444).copy(alpha = 0.15f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Error icon with animation
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFEF4444).copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("❌", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "Kuch Gadbad Ho Gayi!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Error message card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D0A0A).copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Error Details", color = Color(0xFFEF4444), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        message.ifBlank { "Server se response nahi mila. Please internet connection check karein aur dobara koshish karein." },
                        color = SoftWhite.copy(alpha = 0.75f),
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Suggestion text
            Text(
                "💡 Agar ye issue baar baar aa raha hai toh thoda wait karke retry karein. Server busy ho sakta hai.",
                color = GoldAccent.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Retry button
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Dobara Scan Karein", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("🏠  Home Pe Jao", fontSize = 15.sp, color = SoftWhite)
            }
        }
    }
}

// ── Premium reading result screen ───────────────────────────────────────────
@Composable
fun ReadingResultScreen(reading: String, onReset: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(700), label = "result_fade"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        // Gold glowing header
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(GoldAccent.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🔮", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Aapka Haath Padha Gaya!",
            color = GoldAccent,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(GoldAccent.copy(alpha = 0.5f), Offset(0f, 2f), 8f)
            )
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "AI-powered deep palm reading",
            color = SoftWhite.copy(alpha = 0.5f),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reading card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonViolet, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Palm Reading", color = NeonViolet, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = reading,
                    color = SoftWhite,
                    fontSize = 15.sp,
                    lineHeight = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Line scores (decorative)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark.copy(alpha = 0.7f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Palm Lines", color = GoldAccent, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                listOf(
                    "❤️  Hridaya Rekha (Heart)" to 0.78f,
                    "🧠  Mastishk Rekha (Head)" to 0.65f,
                    "🌿  Jeevan Rekha (Life)" to 0.82f,
                    "⭐  Bhagya Rekha (Fate)" to 0.55f,
                ).forEach { (label, score) ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, color = SoftWhite.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text("${(score * 100).toInt()}%", color = NeonViolet, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { score },
                            modifier = Modifier.fillMaxWidth().height(5.dp),
                            color = NeonViolet,
                            trackColor = SoftWhite.copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
        ) {
            Text("✋  Naya Reading Lein", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
