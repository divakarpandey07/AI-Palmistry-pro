package com.example.palmistry.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object PdfReportExporter {

    fun generateAndSharePdf(context: Context, readingResult: String, title: String = "AI Hastrekha Report") {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintTitle = Paint().apply {
            color = Color.rgb(26, 10, 46) // DeepPurple
            textSize = 20f
            isFakeBoldText = true
        }

        val paintBody = Paint().apply {
            color = Color.DKGRAY
            textSize = 12f
        }

        // Draw Header
        canvas.drawText("✨ AI PALMISTRY PRO - SAMUDRIK SHASTRA REPORT", 40f, 50f, paintTitle)
        canvas.drawLine(40f, 65f, 555f, 65f, paintTitle)

        // Split text lines
        var y = 90f
        val lines = readingResult.split("\n")
        for (line in lines) {
            if (y > 800f) break
            canvas.drawText(line.take(80), 40f, y, paintBody)
            y += 18f
        }

        pdfDocument.finishPage(page)

        // Save PDF File
        val pdfFile = File(context.cacheDir, "Hastrekha_Report_${System.currentTimeMillis()}.pdf")
        try {
            FileOutputStream(pdfFile).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Hastrekha PDF Report"))
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
        }
    }
}
