package com.example.themoviedb.ui.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mutableMainProgress.observe(this) {
//            progress_bar.visibility = it
//            Log.e("Progress bar ","$it")
//        }
    }
    companion object{
        val mutableMainProgress = MutableLiveData(View.VISIBLE)
    }
}