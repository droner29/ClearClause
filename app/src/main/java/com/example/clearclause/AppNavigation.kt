
package com.example.clearclause

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clearclause.model.AnalysisResult
import com.example.clearclause.uit.UploadScreen

private enum class AppScreen {
    HOME,
    UPLOAD,
    RESULTS
}

@Composable
fun AppNavigation() {
    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    val context = LocalContext.current
    val contractViewModel: ContractViewModel = viewModel()

    val result by contractViewModel.analysisResult
    val isLoading by contractViewModel.isLoading
    val error by contractViewModel.errorMessage

    when (currentScreen) {
        AppScreen.HOME -> {
            HomeScreen(
                onUploadClick = {
                    currentScreen = AppScreen.UPLOAD
                }
            )
        }

        AppScreen.UPLOAD -> {
            UploadScreen(
                onAnalyze = { uri ->
                    contractViewModel.analyzeContract(uri)
                    currentScreen = AppScreen.RESULTS
                }
            )
        }

        AppScreen.RESULTS -> {
            ResultsScreen(
                result = result,
                isLoading = isLoading,
                errorMessage = error,
                onBack = {
                    currentScreen = AppScreen.HOME
                },
                onUploadAnother = {
                    contractViewModel.clearAnalysis()
                    currentScreen = AppScreen.UPLOAD
                }
            )
        }
    }
}

@Composable
private fun ResultsScreen(
    result: AnalysisResult?,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onUploadAnother: () -> Unit
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
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text("Analyzing your contract with AI…")
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            result != null -> {
                AnalysisSection("Summary", result.summary)
                AnalysisListSection("Important Clauses", result.importantClauses)
                AnalysisListSection("Potential Risks", result.riskyClauses)
                AnalysisListSection("Your Obligations", result.obligations)
                AnalysisListSection("Questions to Ask", result.questionsToAsk)

                Text(
                    text = "This is an AI-generated explanation, not legal advice. " +
                            "Review important decisions with a qualified lawyer.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            else -> {
                Text("Select a PDF to begin your analysis.")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onUploadAnother,
            enabled = !isLoading
        ) {
            Text("Upload Another Contract")
        }

        OutlinedButton(
            onClick = onBack,
            enabled = !isLoading
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
private fun AnalysisSection(
    title: String,
    content: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(content, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
    }
}

@Composable
private fun AnalysisListSection(
    title: String,
    items: List<String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)

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
