package com.example.palmistry.ui

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

@Composable
fun CameraScreen(
    selectedLanguage: String,
    onPalmMetadataReady: (String) -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isFlashOn by remember { mutableStateOf(false) }
    var cameraControl: Camera? by remember { mutableStateOf(null) }
    var userQuestionText by remember { mutableStateOf("") }

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

        // Animated Palm Overlay Guide
        AnimatedPalmOverlay()

        // Top Action Bar: Home Button & Flashlight Toggle Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
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

        // Bottom Controls: Optional Question Input & Capture / Scan Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional Specific Question Input (e.g. Shadi kab hogi / Job kab lagegi)
            OutlinedTextField(
                value = userQuestionText,
                onValueChange = { userQuestionText = it },
                placeholder = { Text("Poshain: Shadi, Job ya Dhan kab milne ka yog hai...", color = SoftWhite.copy(alpha = 0.6f), fontSize = 13.sp) },
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

            // Capture / Scan Palm Photo Button
            Button(
                onClick = {
                    val finalQuestion = if (userQuestionText.isNotBlank()) userQuestionText else "Aapka poora Hastrekha Bhavishyavani vivran vistar se dein."
                    val sampleMetadata = """{
                        "hand": "Right",
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
                        text = "CLICK PHOTO & SCAN PALM",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPurple
                    )
                }
            }
        }
    }
}
