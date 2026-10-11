
package com.example.clearclause.uit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clearclause.data.local.ContractEntity
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryScreen(
    contracts: List<ContractEntity>,
    onOpenContract: (ContractEntity) -> Unit,
    onDeleteContract: (ContractEntity) -> Unit,
    onBack: () -> Unit
) {
    var contractToDelete by remember {
        mutableStateOf<ContractEntity?>(null)
    }

    contractToDelete?.let { contract ->
        AlertDialog(
            onDismissRequest = { contractToDelete = null },
            title = { Text("Delete analysis?") },
            text = {
                Text("This will remove this saved analysis from your history.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteContract(contract)
                        contractToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { contractToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Analysis History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.padding(6.dp))

        Button(onClick = onBack) {
            Text("Back to Home")
        }

        if (contracts.isEmpty()) {
            Text(
                text = "No saved analyses yet. Your saved contracts will appear here.",
                modifier = Modifier.padding(top = 20.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = contracts,
                    key = { it.id }
                ) { contract ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenContract(contract) },
                        colors = CardDefaults.cardColors()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = contract.fileName,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = DateFormat.getDateTimeInstance().format(
                                    Date(contract.analyzedAt)
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = contract.summary,
                                maxLines = 3,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = {
                                        contractToDelete = contract
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
