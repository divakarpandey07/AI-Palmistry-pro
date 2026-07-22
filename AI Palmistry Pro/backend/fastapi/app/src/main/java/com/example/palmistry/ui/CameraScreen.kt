package com.example.palmistry.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.palmistry.ui.theme.*
import java.util.*

@Composable
fun CameraScreen(
    selectedLanguage: String,
    onPalmMetadataReady: (String) -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isFlashOn by remember { mutableStateOf(false) }
    var selectedHand by remember { mutableStateOf("Right (Present Karma)") } // Dual Hand Selector
    var cameraControl: Camera? by remember { mutableStateOf(null) }
    var userQuestionText by remember { mutableStateOf("") }
    var isListeningVoice by remember { mutableStateOf(false) }

    // Speech-To-Text Voice Input Recognizer
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    DisposableEffect(context) {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { isListeningVoice = true }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListeningVoice = false }
            override fun onError(error: Int) { isListeningVoice = false }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    userQuestionText = matches[0]
                }
                isListeningVoice = false
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer = recognizer
        onDispose { recognizer.destroy() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            update = { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        val cam = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )
                        cameraControl = cam
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Use case binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(context))
            },
            modifier = Modifier.fillMaxSize()
        )

        // Animated Palm Overlay Guide with Visual Line Highlighting
        AnimatedPalmOverlay(isHighlighterMode = true)

        // Top Action Bar: Home, Flashlight, Dual Hand Selector (Left/Right)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🏠 Home Button
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .size(48.dp)
                        .background(CardDark.copy(alpha = 0.85f), shape = CircleShape)
                ) {
                    Text(text = "🏠", fontSize = 20.sp)
                }

                // Dual Hand Selector Chip (Left / Right)
                Row(
                    modifier = Modifier
                        .background(CardDark.copy(alpha = 0.85f), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    FilterChip(
                        selected = selectedHand.contains("Right"),
                        onClick = { selectedHand = "Right (Present Karma)" },
                        label = { Text("✋ Right Hand", fontSize = 11.sp, color = SoftWhite) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldAccent)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = selectedHand.contains("Left"),
                        onClick = { selectedHand = "Left (Inherited Destiny)" },
                        label = { Text("🤚 Left Hand", fontSize = 11.sp, color = SoftWhite) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldAccent)
                    )
                }

                // ⚡ Flashlight Toggle Button
                IconButton(
                    onClick = {
                        isFlashOn = !isFlashOn
                        cameraControl?.cameraControl?.enableTorch(isFlashOn)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (isFlashOn) GoldAccent else CardDark.copy(alpha = 0.85f),
                            shape = CircleShape
                        )
                ) {
                    Text(text = if (isFlashOn) "⚡ ON" else "💡 OFF", fontSize = 14.sp, color = if (isFlashOn) DeepPurple else SoftWhite)
                }
            }
        }

        // Bottom Controls: Speech Voice Mic Input & Click Photo Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question Input with 🎙️ Voice Mic Button
            OutlinedTextField(
                value = userQuestionText,
                onValueChange = { userQuestionText = it },
                placeholder = { Text("Poochhein: Shadi, Job ya Dhan Yog...", color = SoftWhite.copy(alpha = 0.6f), fontSize = 13.sp) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                            }
                            speechRecognizer?.startListening(intent)
                        }
                    ) {
                        Text(if (isListeningVoice) "🎙️..." else "🎙️", fontSize = 20.sp, color = if (isListeningVoice) GoldAccent else SoftWhite)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardDark.copy(alpha = 0.9f),
                    unfocusedContainerColor = CardDark.copy(alpha = 0.8f),
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = NeonViolet,
                    focusedTextColor = SoftWhite,
                    unfocusedTextColor = SoftWhite
                )
            )

            // Capture & Scan Palm Photo Button
            Button(
                onClick = {
                    val finalQuestion = if (userQuestionText.isNotBlank()) userQuestionText else "Aapka poora $selectedHand Hastrekha Bhavishyavani vivran vistar se dein."
                    val sampleMetadata = """{
                        "hand": "$selectedHand",
                        "language": "$selectedLanguage",
                        "userQuestion": "$finalQuestion",
                        "lifeLineScore": 0.88,
                        "heartLineScore": 0.92,
                        "headLineScore": 0.85,
                        "fateLineScore": 0.80,
                        "confidenceScore": 0.90
                    }""".trimIndent()
                    onPalmMetadataReady(sampleMetadata)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📸", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "SCAN $selectedHand PALM",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPurple
                    )
                }
            }
        }
    }
}
