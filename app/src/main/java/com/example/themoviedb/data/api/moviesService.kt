package com.example.themoviedb.data.api

import com.example.themoviedb.data.model.response.UpcomingMoviesResponse
import retrofit2.http.*


interface   moviesService {
    @GET("upcoming?api_key=f52f4a0bd6f7db6d9bab750909ed1736&language=en-US")
    suspend fun upcomingMovies(@Query("page") page : Int): UpcomingMoviesResponse

}
