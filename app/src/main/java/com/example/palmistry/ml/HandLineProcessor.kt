package com.example.palmistry.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandLineProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interpreter: Interpreter? = null
    private val MODEL_FILE_NAME = "palm_lines_custom_v1.tflite"

    init {
        try {
            val options = Interpreter.Options().apply {
                numThreads = 4 // CPU parallel processing for speed
            }
            interpreter = Interpreter(loadModelFile(context, MODEL_FILE_NAME), options)
            Log.d("HandLineProcessor", "TFLite Model Loaded Successfully")
        } catch (e: Exception) {
            Log.e("HandLineProcessor", "Error loading model: ${e.message}")
        }
    }

    /**
     * Memory‑maps the TFLite model from the assets folder (RAM‑efficient).
     */
    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Takes a cropped 224x224 palm bitmap and MediaPipe landmarks, runs inference,
     * and returns a structured JSON object containing line/mount metadata.
     */
    fun generatePalmistryMetadata(palmBitmap: Bitmap, landmarks: List<NormalizedLandmark>): JSONObject {
        val jsonResult = JSONObject()

        if (interpreter == null) {
            jsonResult.put("error", "Model not initialized")
            return jsonResult
        }

        try {
            // 1. Pre‑process image: resize to 224x224 and normalize 0‑255 → 0.0‑1.0
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0.0f, 255.0f))
                .build()

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(palmBitmap)
            tensorImage = imageProcessor.process(tensorImage)

            // 2. Prepare output buffer – assuming model outputs 10 float values
            val outputBuffer = Array(1) { FloatArray(10) }

            // 3. Run inference
            interpreter?.run(tensorImage.buffer, outputBuffer)

            // 4. Map raw output to meaningful fields
            val raw = outputBuffer[0]

            val lifeLine = JSONObject().apply {
                put("depth", if (raw[0] > 0.5f) "deep" else "shallow")
                put("length", if (raw[1] > 0.5f) "long" else "short")
                put("breaks", raw[2] > 0.7f)
            }

            val heartLine = JSONObject().apply {
                put("curve", if (raw[3] > 0.6f) "curved_to_jupiter" else "straight")
                put("thickness", if (raw[4] > 0.5f) "thick" else "thin")
            }

            val mounts = JSONObject().apply {
                put("jupiter_prominent", raw[5] > 0.6f)
                put("venus_prominent", raw[6] > 0.6f)
                put("moon_prominent", raw[7] > 0.6f)
            }

            jsonResult.put("life_line", lifeLine)
            jsonResult.put("heart_line", heartLine)
            jsonResult.put("mounts", mounts)

            // Optional: add palm shape derived from landmarks
            jsonResult.put("palm_shape", calculatePalmShape(landmarks))
        } catch (e: Exception) {
            Log.e("HandLineProcessor", "Inference error: ${e.message}")
            jsonResult.put("error", "Inference failed")
        }

        return jsonResult
    }

    /**
     * Simple heuristic using MediaPipe hand landmarks to describe overall palm shape.
     */
    private fun calculatePalmShape(landmarks: List<NormalizedLandmark>): String {
        // Example: ratio of palm width to height
        val wristY = landmarks[0].y()
        val middleFingerBaseY = landmarks[9].y()
        val indexBaseX = landmarks[5].x()
        val pinkyBaseX = landmarks[17].x()

        val height = kotlin.math.abs(wristY - middleFingerBaseY)
        val width = kotlin.math.abs(indexBaseX - pinkyBaseX)

        return if (height > width * 1.2) "rectangular_water_fire" else "square_earth_air"
    }

    /**
     * Release the interpreter when no longer needed.
     */
    fun close() {
        interpreter?.close()
    }
}
