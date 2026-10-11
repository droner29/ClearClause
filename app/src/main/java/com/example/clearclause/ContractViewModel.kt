package com.example.clearclause

class ContractViewModel {
}

package com.example.clearclause

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearclause.data.document.PdfTextExtractor
import com.example.clearclause.model.AnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContractViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val pdfTextExtractor = PdfTextExtractor(application)

    private val repository: GeminiContractRepository =
        GeminiContractRepositoryImpl(BuildConfig.GEMINI_API_KEY)

    var isLoading = androidx.compose.runtime.mutableStateOf(false)
        private set

    var analysisResult =
        androidx.compose.runtime.mutableStateOf<AnalysisResult?>(null)
        private set

    var errorMessage =
        androidx.compose.runtime.mutableStateOf<String?>(null)
        private set

    fun analyzeContract(uri: Uri) {
        if (isLoading.value) return

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            analysisResult.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    val contractText = pdfTextExtractor.extractText(uri)
                    repository.analyzeContract(contractText)
                }

                analysisResult.value = result
            } catch (exception: Exception) {
                errorMessage.value =
                    exception.message ?: "Contract analysis failed. Please try again."
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearAnalysis() {
        analysisResult.value = null
        errorMessage.value = null
    }
}
