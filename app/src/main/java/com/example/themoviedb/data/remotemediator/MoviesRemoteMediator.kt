package com.example.themoviedb.data.remotemediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.themoviedb.data.api.moviesService
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.data.model.RemoteKeys
import com.example.themoviedb.data.room.AppDatabase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class MoviesRemoteMediator @Inject constructor(private val api : moviesService, private val appDatabase: AppDatabase): RemoteMediator<Int, Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (appDatabase.movieDao().count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: 1
            val apiResponse = api.upcomingMovies(page)

            val moviesList = apiResponse.results

            val endOfPaginationReached =
                apiResponse.results.isEmpty()

            appDatabase.withTransaction {
                val nextKey = page + 1

                appDatabase.remoteKeysDao().insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                appDatabase.movieDao().insertAllMovies(moviesList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
    private suspend fun getKey(): RemoteKeys? {
        return appDatabase.remoteKeysDao().getKeys().firstOrNull()
    }
}