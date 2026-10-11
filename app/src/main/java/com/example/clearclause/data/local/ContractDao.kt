
package com.example.clearclause.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContract(contract: ContractEntity): Long

    @Query("SELECT * FROM contracts ORDER BY analyzedAt DESC")
    fun getAllContracts(): Flow<List<ContractEntity>>

    @Query("SELECT * FROM contracts WHERE id = :contractId LIMIT 1")
    suspend fun getContractById(contractId: Long): ContractEntity?

    @Delete
    suspend fun deleteContract(contract: ContractEntity)

    @Query("DELETE FROM contracts")
    suspend fun deleteAllContracts()
}
