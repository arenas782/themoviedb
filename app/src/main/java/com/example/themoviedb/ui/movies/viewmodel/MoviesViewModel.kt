package com.example.themoviedb.ui.movies.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.data.repository.MoviesRepository
import com.example.themoviedb.ui.movies.adapter.MovieAdapter
import com.example.themoviedb.utils.EventLiveData
import com.example.themoviedb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository : MoviesRepository) : ViewModel() {



    private var _postDetails = MutableLiveData<Movie>()
    var movieDetails : LiveData<Movie> = _postDetails

    private var _actionDetailsPost = MutableLiveData<EventLiveData<Boolean>>()
    var actionDetailsPost: LiveData<EventLiveData<Boolean>> = _actionDetailsPost

    private var currentResult: Flow<PagingData<Movie>>? = null
    private var currentResultLiveData: LiveData<PagingData<Movie>>? = null


    @ExperimentalPagingApi
    fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        val newResult: Flow<PagingData<Movie>> =
            repository.getMovies().cachedIn(viewModelScope)
        currentResult = newResult

        return newResult
    }

    fun getMovieDetails(){

    }

     fun deleteMovie(id : Int){
         CoroutineScope(Dispatchers.IO).launch {
             Log.e("TAG","deleting post")
             repository.deleteMovieById(id)
         }
    }
}