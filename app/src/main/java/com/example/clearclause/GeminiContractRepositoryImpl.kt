
package com.example.clearclause

import com.example.clearclause.model.AnalysisResult
import com.google.ai.client.generativeai.GenerativeModel
import org.json.JSONObject
import org.json.JSONArray

class GeminiContractRepositoryImpl(
    apiKey: String
) : GeminiContractRepository {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey
    )

    override suspend fun analyzeContract(
        contractText: String
    ): AnalysisResult {
        require(contractText.isNotBlank()) {
            "Contract text cannot be empty."
        }

        val prompt = """
            You are ClearClause, an assistant that explains contracts
            in simple language. You are not a lawyer and must not give
            definitive legal advice.

            Analyze the contract below. Return ONLY valid JSON with
            exactly these five string-array/string fields:
            {
              "summary": "A short plain-language summary",
              "importantClauses": ["..."],
              "riskyClauses": ["..."],
              "obligations": ["..."],
              "questionsToAsk": ["..."]
            }

            Identify potentially concerning clauses without claiming
            that a clause is illegal. If something is unclear, say so.

            CONTRACT:
            $contractText
        """.trimIndent()

        val response = model.generateContent(prompt)
        val responseText = response.text
            ?: throw IllegalStateException("Gemini returned an empty response.")

        val json = JSONObject(
            responseText
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
        )

        return AnalysisResult(
            summary = json.getString("summary"),
            importantClauses = json.getJSONArray("importantClauses").toStringList(),
            riskyClauses = json.getJSONArray("riskyClauses").toStringList(),
            obligations = json.getJSONArray("obligations").toStringList(),
            questionsToAsk = json.getJSONArray("questionsToAsk").toStringList()
        )
    }

    private fun JSONArray.toStringList(): List<String> =
        List(length()) { index -> getString(index) }
}
