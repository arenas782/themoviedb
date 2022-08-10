package com.example.themoviedb.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedb.data.model.Movie


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie ")
    fun getMovies(): PagingSource<Int,Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie : Movie?)

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteMovieById(id: Int)

    suspend fun insertAllMovies(movies: List<Movie?>?){

        movies?.forEach {
            insertMovie(it.apply {
                this!!.createdAt = System.currentTimeMillis()
            })
        }
    }

    @Query("SELECT COUNT(id) from movie")
    suspend fun count(): Int
}