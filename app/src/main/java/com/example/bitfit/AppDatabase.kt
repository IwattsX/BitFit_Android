package com.example.bitfit

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context // Corrected import

@Database(entities = [BitFitEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): BitFitDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "BitFit-db"
            ).build()
    }
}