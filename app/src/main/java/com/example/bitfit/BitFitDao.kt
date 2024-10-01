package com.example.bitfit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BitFitDao {
    @Query("SELECT * FROM BitFit_table")
    fun getAll(): Flow<List<BitFitEntity>>

    @Insert
    fun insertAll(bitfit: List<BitFitEntity>)

    @Query("DELETE FROM BitFit_table")
    fun deleteAll()

    @Insert
    fun insert(bitfit : BitFitEntity)
}