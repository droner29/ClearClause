
package com.example.clearclause.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContractEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContractDatabase : RoomDatabase() {

    abstract fun contractDao(): ContractDao

    companion object {
        @Volatile
        private var INSTANCE: ContractDatabase? = null

        fun getDatabase(context: Context): ContractDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ContractDatabase::class.java,
                    "clearclause_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
