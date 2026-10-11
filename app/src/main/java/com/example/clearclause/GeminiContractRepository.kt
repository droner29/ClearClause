
package com.example.clearclause

import com.example.clearclause.model.AnalysisResult

interface GeminiContractRepository {
    suspend fun analyzeContract(contractText: String): AnalysisResult
}
