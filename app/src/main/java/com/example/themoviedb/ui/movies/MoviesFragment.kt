package com.example.themoviedb.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.data.model.Movie
import com.example.themoviedb.databinding.FragmentMoviesBinding
import com.example.themoviedb.ui.base.BaseFragment
import com.example.themoviedb.ui.movies.adapter.MovieAdapter
import com.example.themoviedb.ui.movies.adapter.MovieLoadingStateAdapter
import com.example.themoviedb.ui.movies.adapter.SwipeGesture
import com.example.themoviedb.ui.movies.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment  : BaseFragment(){
    lateinit var binding : FragmentMoviesBinding
    lateinit var viewModel : MoviesViewModel
    private val adapter =
        MovieAdapter { movie : Movie -> goToDetails(movie) }
    private var searchJob: Job? = null

    private val tempList :MutableList<Movie>  = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoviesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        return binding.root

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[MoviesViewModel::class.java]
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  setupSwipeLayout()

        setUpAdapter()
        subscribe()

    }

    private fun goToDetails(movie : Movie){
        findNavController().navigate( MoviesFragmentDirections.actionMainFragmentToDetailsFragment(movie.poster_path,movie.title,movie.overview))
    }

    @ExperimentalPagingApi
    private fun subscribe(){
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getUpcomingMovies()
                .collectLatest {
                    adapter.submitData(it)

                    it.map { movie ->
                     tempList.add(movie) }
                }
        }

    }
    companion object {
        const val TAG =  "MoviesFragment"
    }

    private fun setUpAdapter() {

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }

        val swipeGesture = object : SwipeGesture(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.position
                viewModel.deleteMovie(adapter.getMovieId(itemPosition))
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recycler)

        binding.recycler.adapter = adapter.withLoadStateFooter(
            footer = MovieLoadingStateAdapter { retry() }
        )


        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false

            } else {
                binding.progress.isVisible = false
                

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                    }

                }

            }
        }

    }
    private fun retry() {
        adapter.retry()
    }
}