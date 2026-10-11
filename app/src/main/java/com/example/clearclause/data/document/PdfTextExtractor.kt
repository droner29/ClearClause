
package com.example.clearclause.data.document

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.IOException

class PdfTextExtractor(private val context: Context) {

    fun extractText(uri: Uri): String {
        PDFBoxResourceLoader.init(context.applicationContext)

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to open the selected PDF.")

        inputStream.use { stream ->
            val document = try {
                PDDocument.load(stream)
            } catch (e: Exception) {
                throw IOException(
                    "Unable to read this PDF. The file may be invalid or password-protected.",
                    e
                )
            }

            document.use { pdf ->
                if (pdf.isEncrypted) {
                    throw IOException(
                        "Password-protected PDFs are not supported."
                    )
                }

                val pageCount = pdf.numberOfPages

                if (pageCount == 0) {
                    throw IOException("This PDF contains no pages.")
                }

                val extractedText = try {
                    PDFTextStripper().getText(pdf).trim()
                } catch (e: Exception) {
                    throw IOException("Failed to extract text from the PDF.", e)
                }

                if (extractedText.isBlank()) {
                    throw IOException(
                        "No readable text was found. This may be a scanned PDF."
                    )
                }

                return extractedText
            }
        }
    }
}
