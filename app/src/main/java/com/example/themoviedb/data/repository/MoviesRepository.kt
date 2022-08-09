package com.example.themoviedb.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.themoviedb.data.api.moviesService
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.data.remotemediator.MoviesRemoteMediator
import com.example.themoviedb.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MoviesRepository @Inject constructor(private val db: AppDatabase, private val moviesService: moviesService) {

    private val pagingSourceFactory = {
        db.movieDao().getMovies()
    }


    @ExperimentalPagingApi
    fun getMovies():Flow<PagingData<Movie>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                jumpThreshold = 20,
                initialLoadSize = 40
            ),
            remoteMediator = MoviesRemoteMediator(
                moviesService,
                db),
            pagingSourceFactory = pagingSourceFactory).flow
    }


    suspend fun deleteMovieById(id : Int) {
        try {
            db.movieDao().deleteMovieById(id)
        }catch (e : Exception){
            Log.e("TAG", "$e.localizedMessage")
        }
    }
}