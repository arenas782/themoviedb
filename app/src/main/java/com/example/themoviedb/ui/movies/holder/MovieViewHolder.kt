package com.example.themoviedb.ui.movies.holder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.themoviedb.R
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.databinding.ItemMovieBinding

import com.example.themoviedb.utils.Constants


class MovieViewHolder(private val binding : ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie : Movie,callback :(Movie) -> Unit) {
        Log.e("tag","binding")

        binding.container.setOnClickListener {
            callback.invoke(movie)
        }
        binding.item = movie
        binding.container.tag = movie.id
        binding.ivPoster.load(Constants.BASE_URL_POSTS.plus(movie.poster_path)){
            crossfade(true)
            placeholder(R.drawable.ic_100tb)
            scale(Scale.FILL)
        }
        binding.executePendingBindings()
    }
}