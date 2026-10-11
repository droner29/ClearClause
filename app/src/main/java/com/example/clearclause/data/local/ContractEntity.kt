
package com.example.clearclause.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contracts")
data class ContractEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val summary: String,
    val importantClauses: String,
    val riskyClauses: String,
    val obligations: String,
    val questionsToAsk: String,
    val analyzedAt: Long = System.currentTimeMillis()
)
