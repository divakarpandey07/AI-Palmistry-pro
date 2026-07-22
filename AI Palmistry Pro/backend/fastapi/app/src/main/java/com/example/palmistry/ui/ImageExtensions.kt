package com.example.palmistry.ui

import android.graphics.BitmapFactory
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

/**
 * Extension function to convert ImageProxy (YUV_420_888) to Bitmap.
 * Uses the first plane (Y-plane) for a quick grayscale conversion.
 * For production, use a full YUV→RGB conversion.
 */
fun ImageProxy.toBitmap(): android.graphics.Bitmap? {
    val buffer: ByteBuffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
