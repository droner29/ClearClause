
package com.example.clearclause.model

data class AnalysisResult(
    val summary: String,
    val importantClauses: List<String>,
    val riskyClauses: List<String>,
    val obligations: List<String>,
    val questionsToAsk: List<String>
)
