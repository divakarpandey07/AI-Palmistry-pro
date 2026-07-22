package com.example.palmistry.ui

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.palmistry.ml.HandLandmarkerHelper
import com.example.palmistry.ml.HandLineProcessor
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors

@Composable
fun CameraScreen(onPalmMetadataReady: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = androidx.camera.view.PreviewView(context)

    // Assume CAMERA permission has been granted by the host Activity.
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // ----- Preview -----
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // ----- Image Analyzer -----
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val handHelper = HandLandmarkerHelper(context)
            val handProcessor = HandLineProcessor(context)

            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                processImageProxy(imageProxy, handHelper, handProcessor, onPalmMetadataReady)
            }

            // Bind to lifecycle
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

private fun processImageProxy(
    imageProxy: ImageProxy,
    handHelper: HandLandmarkerHelper,
    handProcessor: HandLineProcessor,
    callback: (String) -> Unit
) {
    val bitmap = imageProxy.toBitmap() ?: run { imageProxy.close(); return }
    // MediaPipe hand landmarker extracts the palm ROI.
    val palmBitmap = handHelper.extractPalmRegion(bitmap)
    if (palmBitmap != null) {
        // Run the TFLite model and get JSON metadata.
        val json = handProcessor.process(palmBitmap)
        callback(json)
    }
    imageProxy.close()
}

// Simple conversion from ImageProxy to Bitmap (placeholder – real implementation may need YUV to RGB conversion).
private fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
