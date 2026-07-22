package com.example.palmistry.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
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
            val options = HandLandmarker.HandLandmarkerOptions.builder()
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
                val landmarks = result.landmarks().firstOrNull()

                if (!landmarks.isNullOrEmpty()) {
                    var minX = Float.MAX_VALUE
                    var minY = Float.MAX_VALUE
                    var maxX = Float.MIN_VALUE
                    var maxY = Float.MIN_VALUE

                    for (landmark in landmarks) {
                        minX = minOf(minX, landmark.x())
                        minY = minOf(minY, landmark.y())
                        maxX = maxOf(maxX, landmark.x())
                        maxY = maxOf(maxY, landmark.y())
                    }

                    val width = bitmap.width
                    val height = bitmap.height

                    val cropLeft = (minX * width).coerceIn(0f, width.toFloat()).toInt()
                    val cropTop = (minY * height).coerceIn(0f, height.toFloat()).toInt()
                    val cropWidth = ((maxX - minX) * width).coerceIn(1f, (width - cropLeft).toFloat()).toInt()
                    val cropHeight = ((maxY - minY) * height).coerceIn(1f, (height - cropTop).toFloat()).toInt()

                    return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropWidth, cropHeight)
                }
            } catch (e: Exception) {
                Log.w("HandLandmarkerHelper", "Landmark extraction error, falling back to center crop: ${e.message}")
            }
        }

        // Fallback: 60% center crop of input image
        val marginX = (bitmap.width * 0.2f).toInt()
        val marginY = (bitmap.height * 0.2f).toInt()
        val cropW = (bitmap.width * 0.6f).toInt()
        val cropH = (bitmap.height * 0.6f).toInt()

        return Bitmap.createBitmap(
            bitmap,
            marginX.coerceIn(0, bitmap.width - 1),
            marginY.coerceIn(0, bitmap.height - 1),
            cropW.coerceIn(1, bitmap.width - marginX),
            cropH.coerceIn(1, bitmap.height - marginY)
        )
    }
}
