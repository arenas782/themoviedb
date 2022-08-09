package com.example.themoviedb.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.databinding.ItemMovieBinding

import com.example.themoviedb.ui.movies.holder.MovieViewHolder


class MovieAdapter(private val callback: (Movie) -> Unit) :
    PagingDataAdapter<Movie, MovieViewHolder>(
        MoviesClickCallback()
    ) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data!!,callback)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun getMovieId(position: Int):Int = getItem(position)?.id!!

    private class MoviesClickCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

}