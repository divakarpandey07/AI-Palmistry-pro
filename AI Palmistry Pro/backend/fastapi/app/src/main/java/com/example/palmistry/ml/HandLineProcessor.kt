package com.example.palmistry.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.NormalizeOp
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the extracted palm line and mount scores.
 */
data class PalmLineResult(
    @SerializedName("lines") val lines: List<Float>,
    @SerializedName("mounts") val mounts: List<Float>,
    @SerializedName("confidence") val confidence: Float
)

/**
 * HandLineProcessor runs the custom TensorFlow Lite model on a 224x224 bitmap of a cropped palm
 * region and returns a JSON string containing line scores, mount scores and an overall confidence.
 *
 * The TFLite model is expected to output a 1‑D float array where the first half corresponds to
 * hand‑line probabilities and the second half to mount (mountain) scores.
 */
class HandLineProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val interpreter: Interpreter by lazy { loadModel() }
    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0f, 255f))
        .build()

    private fun loadModel(): Interpreter {
        // The model file (hand_lines.tflite) must be placed in the assets folder.
        val modelFile = FileUtil.loadMappedFile(context, "hand_lines.tflite")
        return Interpreter(modelFile)
    }

    /**
     * Run inference on the provided bitmap and return a JSON payload.
     */
    fun process(bitmap: Bitmap): String {
        // Convert bitmap to TensorImage and apply preprocessing.
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val processed = imageProcessor.process(tensorImage)

        // Prepare input and output buffers.
        val inputBuffer = processed.tensorBuffer.buffer
        val outputShape = interpreter.getOutputTensor(0).shape() // e.g., [1, 128]
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, interpreter.getOutputTensor(0).dataType())

        // Run the model.
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        // Interpret output.
        val outputArray = outputBuffer.floatArray
        val half = outputArray.size / 2
        val lineScores = outputArray.sliceArray(0 until half)
        val mountScores = outputArray.sliceArray(half until outputArray.size)

        // Confidence is a simple average of the best line and mount scores.
        val confidence = (lineScores.maxOrNull()!! + mountScores.maxOrNull()!!) / 2f

        val result = PalmLineResult(
            lines = lineScores.toList(),
            mounts = mountScores.toList(),
            confidence = confidence
        )
        return Gson().toJson(result)
    }
}
