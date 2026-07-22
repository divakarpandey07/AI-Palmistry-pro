package com.example.palmistry.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.palmistry.data.model.ReadingEntity

@Database(entities = [ReadingEntity::class], version = 1, exportSchema = false)
abstract class PalmistryDatabase : RoomDatabase() {

    abstract fun readingDao(): ReadingDao

    companion object {
        private const val DB_NAME = "palmistry_secure.db"

        @Volatile
        private var INSTANCE: PalmistryDatabase? = null

        fun getInstance(context: Context): PalmistryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PalmistryDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
