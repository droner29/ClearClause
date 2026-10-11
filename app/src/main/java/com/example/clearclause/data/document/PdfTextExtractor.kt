
package com.example.clearclause.data.document

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

class PdfTextExtractor(private val context: Context) {

    fun extractText(uri: Uri): String {
        PDFBoxResourceLoader.init(context)

        val input = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Unable to open the selected PDF.")

        input.use { stream ->
            PDDocument.load(stream).use { document ->

                if (document.isEncrypted) {
                    throw Exception(
                        "Password-protected PDFs are not supported."
                    )
                }

                val text = PDFTextStripper()
                    .getText(document)
                    .trim()

                if (text.isBlank()) {
                    throw Exception(
                        "No readable text found. This PDF may be scanned."
                    )
                }

                return text
            }
        }
    }
}
