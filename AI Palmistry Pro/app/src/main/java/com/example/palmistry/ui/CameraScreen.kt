package com.example.palmistry.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

// ─── Scan phase states ───────────────────────────────────────────────────────
private enum class ScanPhase { PREVIEW, CAPTURED, ANALYZING }

@Composable
fun CameraScreen(onPalmMetadataReady: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // Camera state
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var flashlightOn by remember { mutableStateOf(false) }
    var scanPhase by remember { mutableStateOf(ScanPhase.PREVIEW) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var palmLines by remember { mutableStateOf<List<Pair<Offset, Offset>>>(emptyList()) }
    var lineAlpha by remember { mutableStateOf(0f) }
    var showFlashDialog by remember { mutableStateOf(false) }

    // Camera manager for torch
    val cameraManager = remember {
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    val cameraId = remember {
        cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    }

    fun toggleFlashlight(on: Boolean) {
        try {
            cameraId?.let { cameraManager.setTorchMode(it, on) }
        } catch (e: Exception) {
            Log.e("Camera", "Torch error: ${e.message}")
        }
    }

    // Pulsing scan ring animation
    val infiniteTransition = rememberInfiniteTransition(label = "scan_ring")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 0.85f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ring_scale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Reverse),
        label = "ring_alpha"
    )

    // Line draw animation (for post-capture)
    val lineDrawProgress by animateFloatAsState(
        targetValue = lineAlpha,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "line_draw"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // ── Camera Preview ────────────────────────────────────────────────
        if (scanPhase == ScanPhase.PREVIEW) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val capture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()
                        imageCapture = capture
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, capture
                            )
                        } catch (e: Exception) {
                            Log.e("Camera", "Bind error: ${e.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                }
            )

            // ── Elegant scan frame (NO extra lines, just corner arcs) ──────
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawScanFrame(ringScale, ringAlpha)
            }
        }

        // ── Captured bitmap + palm lines drawn on top ─────────────────────
        if (scanPhase == ScanPhase.CAPTURED || scanPhase == ScanPhase.ANALYZING) {
            capturedBitmap?.let { bmp ->
                androidx.compose.foundation.Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Captured palm",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                // Draw detected palm lines animated on top
                Canvas(modifier = Modifier.fillMaxSize()) {
                    for ((start, end) in palmLines) {
                        drawLine(
                            color = Color(0xFF7C3AED).copy(alpha = lineDrawProgress),
                            start = Offset(start.x * size.width, start.y * size.height),
                            end = Offset(end.x * size.width, end.y * size.height),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                    // Gold center dot
                    if (lineDrawProgress > 0.5f) {
                        drawCircle(
                            color = Color(0xFFF59E0B).copy(alpha = lineDrawProgress),
                            radius = 8.dp.toPx(),
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                }
            }
        }

        // ── Top bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "AI Palmistry",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            // Flashlight button
            if (scanPhase == ScanPhase.PREVIEW) {
                IconButton(
                    onClick = {
                        if (cameraId != null) {
                            flashlightOn = !flashlightOn
                            toggleFlashlight(flashlightOn)
                        } else {
                            showFlashDialog = true
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (flashlightOn) Color(0xFFF59E0B).copy(alpha = 0.2f)
                            else Color.White.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (flashlightOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                        contentDescription = "Flashlight",
                        tint = if (flashlightOn) Color(0xFFF59E0B) else Color.White
                    )
                }
            }
        }

        // ── Center scanning instruction (REMOVED per user request – no hint text) ──

        // ── Bottom controls ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (scanPhase) {
                ScanPhase.PREVIEW -> {
                    // Capture button
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))
                                ),
                                shape = CircleShape
                            )
                            .border(3.dp, Color(0xFFF59E0B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                val capture = imageCapture ?: return@Button
                                scanPhase = ScanPhase.ANALYZING
                                val executor = Executors.newSingleThreadExecutor()
                                capture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        val buffer = image.planes[0].buffer
                                        val bytes = ByteArray(buffer.remaining())
                                        buffer.get(bytes)
                                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                        val rotated = rotateBitmap(bmp, image.imageInfo.rotationDegrees.toFloat())
                                        image.close()
                                        // Generate mock palm lines from image dimensions
                                        val lines = generatePalmLines(rotated.width, rotated.height)
                                        scope.launch {
                                            capturedBitmap = rotated
                                            palmLines = lines
                                            scanPhase = ScanPhase.CAPTURED
                                            // Animate lines appearing
                                            delay(400)
                                            lineAlpha = 1f
                                            // Hold for 5-8 seconds then proceed
                                            delay(6000)
                                            // Create base64 palm JSON to send
                                            val palmJson = bitmapToBase64Json(rotated)
                                            onPalmMetadataReady(palmJson)
                                        }
                                    }
                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e("Camera", "Capture error: ${exception.message}")
                                        scope.launch { scanPhase = ScanPhase.PREVIEW }
                                    }
                                })
                            },
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("📸", fontSize = 28.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Apna haath camera ke saamne rakhen",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }

                ScanPhase.ANALYZING -> {
                    // Processing indicator
                    CircularProgressIndicator(color = Color(0xFF7C3AED), strokeWidth = 3.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("🔮 Palm lines analyze ho rahi hain...", color = Color.White, fontSize = 14.sp)
                }

                ScanPhase.CAPTURED -> {
                    // Lines drawing state
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✨ Palm lines detect ho gayi!", color = Color(0xFFF59E0B), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("AI reading prepare kar raha hai...", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // ── Flashlight not available dialog ───────────────────────────────────────
    if (showFlashDialog) {
        AlertDialog(
            onDismissRequest = { showFlashDialog = false },
            containerColor = Color(0xFF2D1B69),
            title = {
                Text("🔦 Flashlight", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Is device mein flashlight available nahi hai ya permission required hai.",
                    color = Color(0xFFF1F5F9), fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showFlashDialog = false }) {
                    Text("Theek Hai", color = Color(0xFF7C3AED))
                }
            }
        )
    }
}

// ─── Draw elegant scan frame (corner arcs only, NO crossing lines) ─────────
private fun DrawScope.drawScanFrame(scale: Float, alpha: Float) {
    val w = size.width
    val h = size.height
    val frameW = w * 0.75f * scale
    val frameH = h * 0.55f * scale
    val left = (w - frameW) / 2f
    val top = (h - frameH) / 2f
    val right = left + frameW
    val bottom = top + frameH
    val cornerLen = 60.dp.toPx()
    val stroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
    val color = Color(0xFF7C3AED).copy(alpha = alpha)
    val gold = Color(0xFFF59E0B).copy(alpha = alpha * 0.6f)

    // Corner arcs - top left
    drawLine(color, Offset(left, top + cornerLen), Offset(left, top), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(left, top), Offset(left + cornerLen, top), stroke.width, StrokeCap.Round)
    // top right
    drawLine(color, Offset(right - cornerLen, top), Offset(right, top), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(right, top), Offset(right, top + cornerLen), stroke.width, StrokeCap.Round)
    // bottom left
    drawLine(color, Offset(left, bottom - cornerLen), Offset(left, bottom), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(left, bottom), Offset(left + cornerLen, bottom), stroke.width, StrokeCap.Round)
    // bottom right
    drawLine(color, Offset(right - cornerLen, bottom), Offset(right, bottom), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(right, bottom), Offset(right, bottom - cornerLen), stroke.width, StrokeCap.Round)
}

// ─── Generate palm lines (normalized 0-1 coords) ─────────────────────────
private fun generatePalmLines(w: Int, h: Int): List<Pair<Offset, Offset>> {
    // Heart line, head line, life line (approximate positions)
    return listOf(
        // Heart line (upper palm, horizontal)
        Offset(0.2f, 0.35f) to Offset(0.8f, 0.30f),
        // Head line (middle)
        Offset(0.15f, 0.48f) to Offset(0.75f, 0.52f),
        // Life line (curved bottom-left arc – approximated with 3 segments)
        Offset(0.35f, 0.25f) to Offset(0.25f, 0.45f),
        Offset(0.25f, 0.45f) to Offset(0.20f, 0.65f),
        Offset(0.20f, 0.65f) to Offset(0.30f, 0.80f),
        // Fate line (vertical center)
        Offset(0.50f, 0.75f) to Offset(0.48f, 0.38f),
    )
}

// ─── Rotate bitmap ────────────────────────────────────────────────────────
private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    if (degrees == 0f) return bitmap
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

// ─── Encode bitmap to Base64 JSON for API ────────────────────────────────
private fun bitmapToBase64Json(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    // Scale down to reduce payload
    val scaled = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
    scaled.compress(Bitmap.CompressFormat.JPEG, 70, stream)
    val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.NO_WRAP)
    return """{"image_base64":"$base64","width":224,"height":224}"""
}
