
package com.example.clearclause.uit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UploadScreen(onAnalyze: (Uri) -> Unit) {
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        selectedUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Upload Contract",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Select a PDF agreement to understand its clauses."
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                pdfPicker.launch(arrayOf("application/pdf"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose PDF")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (selectedUri != null) {
                "PDF selected successfully."
            } else {
                "No document selected."
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                selectedUri?.let { uri ->
                    onAnalyze(uri)
                }
            },
            enabled = selectedUri != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Analyze Contract")
        }
    }
}
