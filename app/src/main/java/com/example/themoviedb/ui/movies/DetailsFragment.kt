package com.example.themoviedb.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentMovieDetailsBinding
import com.example.themoviedb.ui.base.BaseFragment
import com.example.themoviedb.utils.Constants
import com.example.themoviedb.utils.viewBinding

class DetailsFragment : BaseFragment() {
    private val args : DetailsFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.ivPoster.load(Constants.BASE_URL_POSTS.plus(args.posterPath)){
            crossfade(true)
            placeholder(R.drawable.ic_100tb)
            scale(Scale.FIT)
        }
        binding.tvOverview.text = args.overview
        binding.tvTitle.text = args.title
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}