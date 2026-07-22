package com.example.palmistry.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.palmistry.data.model.ReadingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for reading history stored in encrypted SQLCipher database.
 */
@Dao
interface ReadingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: ReadingEntity)

    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<ReadingEntity>>

    @Query("SELECT * FROM readings WHERE id = :id")
    suspend fun getReadingById(id: Int): ReadingEntity?

    @Query("DELETE FROM readings WHERE id = :id")
    suspend fun deleteReading(id: Int)

    @Query("DELETE FROM readings")
    suspend fun deleteAllReadings()
}
