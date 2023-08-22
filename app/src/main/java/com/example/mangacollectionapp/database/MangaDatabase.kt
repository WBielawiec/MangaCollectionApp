package com.example.mangacollectionapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.mangacollectionapp.models.Manga
import com.example.mangacollectionapp.models.MangaVolume

@Database(
    entities = [Manga::class, MangaVolume::class],
    version = 1
)
abstract class MangaDatabase : RoomDatabase() {

    abstract val mangaDao: MangaDao

    companion object {
        @Volatile
        private var INSTANCE: MangaDatabase? = null

        fun getDatabase(context: Context): MangaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    MangaDatabase::class.java,
                    "manga-database.db"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}



