package com.example.palmistry.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerOptions
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandLandmarkerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val handLandmarker: HandLandmarker? by lazy { buildLandmarker() }

    private fun buildLandmarker(): HandLandmarker? {
        return try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("hand_landmarker.task")
                .build()
            val options = HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setNumHands(1)
                .setMinHandDetectionConfidence(0.6f)
                .setMinHandPresenceConfidence(0.6f)
                .setMinTrackingConfidence(0.6f)
                .setRunningMode(RunningMode.IMAGE)
                .build()
            HandLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            Log.w("HandLandmarkerHelper", "MediaPipe task file not found, using center crop fallback: ${e.message}")
            null
        }
    }

    fun extractPalmRegion(bitmap: Bitmap): Bitmap {
        val landmarker = handLandmarker
        if (landmarker != null) {
            try {
                val mpImage = BitmapImageBuilder(bitmap).build()
                val result: HandLandmarkerResult = landmarker.detect(mpImage)

                if (result.landmarks().isNotEmpty()) {
                    val landmarks = result.landmarks()[0]
                    var minX = Float.MAX_VALUE
                    var minY = Float.MAX_VALUE
                    var maxX = Float.MIN_VALUE
                    var maxY = Float.MIN_VALUE

                    for (lm in landmarks) {
                        if (lm.x() < minX) minX = lm.x()
                        if (lm.y() < minY) minY = lm.y()
                        if (lm.x() > maxX) maxX = lm.x()
                        if (lm.y() > maxY) maxY = lm.y()
                    }

                    val padX = (maxX - minX) * 0.1f
                    val padY = (maxY - minY) * 0.1f

                    val rect = RectF(
                        ((minX - padX) * bitmap.width).coerceAtLeast(0f),
                        ((minY - padY) * bitmap.height).coerceAtLeast(0f),
                        ((maxX + padX) * bitmap.width).coerceAtMost(bitmap.width.toFloat()),
                        ((maxY + padY) * bitmap.height).coerceAtMost(bitmap.height.toFloat())
                    )

                    val cropped = Bitmap.createBitmap(
                        bitmap,
                        rect.left.toInt(),
                        rect.top.toInt(),
                        rect.width().toInt().coerceAtLeast(1),
                        rect.height().toInt().coerceAtLeast(1)
                    )
                    return Bitmap.createScaledBitmap(cropped, 224, 224, true)
                }
            } catch (e: Exception) {
                Log.e("HandLandmarkerHelper", "MediaPipe extraction error: ${e.message}")
            }
        }

        // Center Crop Fallback
        val side = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        val cropped = Bitmap.createBitmap(bitmap, x, y, side, side)
        return Bitmap.createScaledBitmap(cropped, 224, 224, true)
    }
}
