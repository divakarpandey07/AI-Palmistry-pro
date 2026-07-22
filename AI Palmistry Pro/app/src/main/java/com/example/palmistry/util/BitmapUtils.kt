package com.example.palmistry.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

object BitmapUtils {

    /**
     * Rotate bitmap according to camera sensor orientation.
     */
    fun rotateBitmapIfRequired(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees == 0) return bitmap
        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Crop the center square from a bitmap.
     */
    fun cropCenterSquare(bitmap: Bitmap): Bitmap {
        val side = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        return Bitmap.createBitmap(bitmap, x, y, side, side)
    }

    /**
     * Resize bitmap to target dimensions.
     */
    fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap =
        Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)

    /**
     * Convert YUV_420_888 ImageProxy to RGB Bitmap.
     * Uses JPEG compression via ImageProxy.planes as a reliable fallback.
     */
    fun convertYUVtoBitmap(imageProxy: ImageProxy): Bitmap? {
        val yBuffer: ByteBuffer = imageProxy.planes[0].buffer
        val uBuffer: ByteBuffer = imageProxy.planes[1].buffer
        val vBuffer: ByteBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = android.graphics.YuvImage(
            nv21,
            android.graphics.ImageFormat.NV21,
            imageProxy.width,
            imageProxy.height,
            null
        )
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            android.graphics.Rect(0, 0, imageProxy.width, imageProxy.height),
            90,
            out
        )
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}
