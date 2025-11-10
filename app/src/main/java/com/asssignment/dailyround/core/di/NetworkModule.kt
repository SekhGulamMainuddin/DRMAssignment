package com.asssignment.dailyround.core.di

import com.asssignment.dailyround.core.constants.Constants.BASE_URL
import com.asssignment.dailyround.features.quiz.data.datasource.QuizRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).client(OkHttpClient())
        .build()

    @Provides
    @Singleton
    fun provideQuizRemoteService(retrofit: Retrofit): QuizRemoteDataSource {
        return retrofit.create(QuizRemoteDataSource::class.java)
    }
}