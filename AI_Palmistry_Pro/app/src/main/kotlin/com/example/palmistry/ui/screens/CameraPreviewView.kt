package com.example.palmistry.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.palmistry.ml.MediaPipeHelper
import com.example.palmistry.ml.PalmCropUtil
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import java.util.concurrent.Executors

/**
 * Wrapper composable that requests CAMERA permission and, when granted, shows the camera preview.
 */
@Composable
fun CameraPermissionWrapper(
    onHandDetected: (Bitmap, List<NormalizedLandmark>) -> Unit
) {
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasCameraPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {
        CameraPreviewView(onHandDetected = onHandDetected)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission is required to scan your palm.")
        }
    }
}

/**
 * Camera preview composable using CameraX inside an AndroidView.
 * Frames are sent to MediaPipe via ImageAnalysis and the detected hand is cropped and returned.
 */
@Composable
fun CameraPreviewView(
    onHandDetected: (Bitmap, List<NormalizedLandmark>) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    // MediaPipe helper is created once per composition
    val mediaPipeHelper = remember { MediaPipeHelper(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
                setupCamera(
                    ctx,
                    previewView,
                    lifecycleOwner,
                    cameraExecutor,
                    mediaPipeHelper,
                    onHandDetected
                )
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Simple overlay UI guiding the user
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { /* future manual capture if needed */ },
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Align Palm & Hold Still")
            }
        }
    }
}

private fun setupCamera(
    context: Context,
    previewView: PreviewView,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    cameraExecutor: java.util.concurrent.ExecutorService,
    mediaPipeHelper: MediaPipeHelper,
    onHandDetected: (Bitmap, List<NormalizedLandmark>) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Preview UseCase – shows live feed
        val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

        // ImageAnalysis UseCase – processes frames
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    processImageProxy(imageProxy, mediaPipeHelper, onHandDetected)
                }
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Binding use cases failed", e)
        }
    }, ContextCompat.getMainExecutor(context))
}

private fun processImageProxy(
    imageProxy: ImageProxy,
    mediaPipeHelper: MediaPipeHelper,
    onHandDetected: (Bitmap, List<NormalizedLandmark>) -> Unit
) {
    try {
        // Convert ImageProxy (RGBA) to Bitmap
        val buffer = imageProxy.planes[0].buffer
        val bitmap = Bitmap.createBitmap(
            imageProxy.width,
            imageProxy.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)

        // Rotate based on sensor orientation
        val matrix = android.graphics.Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
        }
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
        bitmap.recycle()

        // Run MediaPipe hand detection
        val result = mediaPipeHelper.detectHand(rotatedBitmap)
        if (result != null && result.landmarks().isNotEmpty()) {
            val handLandmarks = result.landmarks()[0]
            // Crop only the palm region for the TFLite model
            val palmBitmap = PalmCropUtil.cropPalmRegion(rotatedBitmap, handLandmarks)
            if (palmBitmap != null) {
                onHandDetected(palmBitmap, handLandmarks)
            }
        } else {
            // No hand detected – free bitmap
            rotatedBitmap.recycle()
        }
    } catch (e: Exception) {
        Log.e("CameraX", "Error processing frame: ${e.message}")
    } finally {
        // Very important: close the image to allow next frame
        imageProxy.close()
    }
}
