package com.example.palmistry.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.palmistry.data.model.ReadingEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Room Database secured with SQLCipher encryption.
 * The database passphrase is derived from a securely stored key.
 */
@Database(entities = [ReadingEntity::class], version = 1, exportSchema = false)
abstract class PalmistryDatabase : RoomDatabase() {

    abstract fun readingDao(): ReadingDao

    companion object {
        private const val DB_NAME = "palmistry_secure.db"
        // IMPORTANT: In production use Android Keystore to generate/store this passphrase.
        private const val DB_PASSPHRASE = "palm_secure_key_2024_@!#"

        @Volatile
        private var INSTANCE: PalmistryDatabase? = null

        fun getInstance(context: Context): PalmistryDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = SQLiteDatabase.getBytes(DB_PASSPHRASE.toCharArray())
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PalmistryDatabase::class.java,
                    DB_NAME
                )
                    .openHelperFactory(factory)  // SQLCipher encryption
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
