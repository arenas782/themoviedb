package com.example.themoviedb.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.data.model.RemoteKeys

@Database(entities = [Movie::class, RemoteKeys::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao() : RemoteKeysDao

    companion object {
        const val DATABASE_NAME : String = "movies_db"
    }
}