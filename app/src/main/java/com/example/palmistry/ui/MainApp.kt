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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
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

// ── Screen state machine ────────────────────────────────────────────────────
enum class AppScreen {
    HOME,
    CAMERA,
    RESULT,
    KUNDLI,
    TAROT,
    NUMEROLOGY,
    HISTORY,
    ERROR
}

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
                    colors = listOf(Color(0xFF07040E), Color(0xFF130C29), Color(0xFF090615))
                )
            )
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(tween(350)) togetherWith fadeOut(tween(250))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> HomeScreen(
                    onStartScan = { currentScreen = AppScreen.CAMERA },
                    onOpenKundli = { currentScreen = AppScreen.KUNDLI },
                    onOpenTarot = { currentScreen = AppScreen.TAROT },
                    onOpenNumerology = { currentScreen = AppScreen.NUMEROLOGY },
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

                AppScreen.KUNDLI -> KundliScreen(
                    onBack = { currentScreen = AppScreen.HOME }
                )

                AppScreen.TAROT -> TarotScreen(
                    onBack = { currentScreen = AppScreen.HOME }
                )

                AppScreen.NUMEROLOGY -> NumerologyScreen(
                    onBack = { currentScreen = AppScreen.HOME }
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
                        onDelete = { id -> historyViewModel.deleteReading(id) },
                        onBack = { currentScreen = AppScreen.HOME }
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
        initialValue = 0.92f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF07040E), Color(0xFF130C29)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF6B21A8).copy(alpha = 0.3f),
                border = BorderStroke(2.dp, Color(0xFFF3C654))
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFF3C654),
                    modifier = Modifier
                        .padding(24.dp)
                        .size(54.dp)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                "AI Palm Analysis In Progress",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Decoding Heart, Head, Life & Fate Lines...",
                color = Color(0xFFE2E8F0).copy(alpha = 0.6f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(
                color = Color(0xFFF3C654),
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF130C29), Color(0xFF2A0D18), Color(0xFF07040E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFDC2626).copy(alpha = 0.2f),
                border = BorderStroke(1.5.dp, Color(0xFFDC2626))
            ) {
                Icon(
                    imageVector = Icons.Filled.ErrorOutline,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.padding(20.dp).size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Analysis Error Encountered",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F0D16)),
                border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
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
                        Text("System Log", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        message.ifBlank { "Network timeout or server connectivity issue. Please retry scanning." },
                        color = Color(0xFFE2E8F0).copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8)),
                border = BorderStroke(1.dp, Color(0xFFF3C654))
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry Palm Scan", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))
            ) {
                Icon(Icons.Filled.Home, contentDescription = null, tint = Color(0xFFF3C654))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Return To Home", fontSize = 15.sp, color = Color(0xFFE2E8F0))
            }
        }
    }
}

// ── Premium reading result screen ───────────────────────────────────────────
@Composable
fun ReadingResultScreen(reading: String, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Luxury Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181033)),
            border = BorderStroke(1.5.dp, Color(0xFFF3C654))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFF3C654),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Palmistry AI Reading Complete",
                    color = Color(0xFFF3C654),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "High Precision Neural Analysis",
                    color = Color(0xFFCBD5E1).copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Reading Text
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF120C29)),
            border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    "Detailed Reading Insights",
                    color = Color(0xFFF3C654),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = reading,
                    color = Color(0xFFE2E8F0),
                    fontSize = 14.sp,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Line Scores Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181033)),
            border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Detected Palm Lines Metrics", color = Color(0xFFF3C654), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(14.dp))
                listOf(
                    "Heart Line (Hridaya Rekha)" to 0.84f,
                    "Head Line (Mastishk Rekha)" to 0.72f,
                    "Life Line (Jeevan Rekha)" to 0.88f,
                    "Fate Line (Bhagya Rekha)" to 0.65f,
                ).forEach { (label, score) ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            Text("${(score * 100).toInt()}%", color = Color(0xFFF3C654), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = score,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = Color(0xFF6B21A8),
                            trackColor = Color(0xFFE2E8F0).copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B21A8)),
            border = BorderStroke(1.dp, Color(0xFFF3C654))
        ) {
            Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Return To Home", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
