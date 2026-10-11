
package com.example.clearclause.uit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clearclause.model.AnalysisResult

@Composable
fun AnalysisScreen(
    result: AnalysisResult?,
    isLoading: Boolean,
    errorMessage: String?,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Contract Analysis",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            isLoading -> {
                CircularProgressIndicator()
                Text("Analyzing your contract…")
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            result != null -> {
                ResultSection("Summary", result.summary)
                ResultList("Important Clauses", result.importantClauses)
                ResultList("Potential Risks", result.riskyClauses)
                ResultList("Obligations", result.obligations)
                ResultList("Questions to Ask", result.questionsToAsk)

                Text(
                    text = "AI-generated explanations are not legal advice.",
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save to History")
                }
            }

            else -> Text("Upload a contract to see its analysis.")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun ResultSection(title: String, content: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(content)
        }
    }
}

@Composable
private fun ResultList(title: String, items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        if (items.isEmpty()) {
            Text("No items identified.")
        } else {
            items.forEachIndexed { index, item ->
                Text("${index + 1}. $item")
            }
        }

        HorizontalDivider()
    }
}
