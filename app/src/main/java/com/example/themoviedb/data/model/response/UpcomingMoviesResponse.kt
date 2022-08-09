package com.example.themoviedb.data.model.response

import com.example.themoviedb.data.model.Movie
import com.squareup.moshi.Json

data class UpcomingMoviesResponse(
    @field:Json(name = "results")
    val results : ArrayList<Movie>,

    @field:Json(name = "page")
    val page : Int,
    )

