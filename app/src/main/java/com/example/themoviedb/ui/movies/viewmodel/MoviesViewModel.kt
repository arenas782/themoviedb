package com.example.themoviedb.ui.movies.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.*
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.data.model.response.MovieDetailsResponse
import com.example.themoviedb.data.repository.MoviesRepository
import com.example.themoviedb.ui.movies.adapter.MovieAdapter
import com.example.themoviedb.utils.EventLiveData
import com.example.themoviedb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository : MoviesRepository) : ViewModel() {



    private var _movieDetails = MutableLiveData<Resource<MovieDetailsResponse>>()
    var movieDetails : LiveData<Resource<MovieDetailsResponse>> = _movieDetails


    private var currentResult: Flow<PagingData<Movie>>? = null





    @ExperimentalPagingApi
    fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        val newResult: Flow<PagingData<Movie>> =
            repository.getMovies().cachedIn(viewModelScope)
        currentResult = newResult

        return newResult
    }

    fun getMovieDetails(movieId : Int) = liveData(Dispatchers.Main) {
            emit(Resource.loading(data = null))
            try {
                val movieDetails = repository.getMovieDetails(movieId = movieId)
                Log.e("movie",movieDetails.toString())
                emit(Resource.success(data = movieDetails))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
    }

     fun deleteMovie(id : Int){
         CoroutineScope(Dispatchers.IO).launch {
             Log.e("TAG","deleting post")
             repository.deleteMovieById(id)
         }
    }

}