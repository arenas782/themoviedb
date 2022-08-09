package com.example.themoviedb.di


import android.content.Context
import androidx.databinding.ktx.BuildConfig
import androidx.room.Room
import com.example.themoviedb.data.api.moviesService
import com.example.themoviedb.data.repository.MoviesRepository
import com.example.themoviedb.data.room.AppDatabase
import com.example.themoviedb.data.room.MovieDao
import com.example.themoviedb.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {




    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): moviesService = retrofit.create(moviesService::class.java)



    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context : Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePostDao(appDatabase: AppDatabase) : MovieDao{
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideMainRepository(db: AppDatabase, moviesService: moviesService) : MoviesRepository{
        return MoviesRepository(db,moviesService)
    }


    @Provides
    fun provideBaseUrl() = Constants.BASE_URL
}