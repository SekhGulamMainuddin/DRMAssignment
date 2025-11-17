package com.asssignment.dailyround.core.di

import com.asssignment.dailyround.core.constants.Constants.BASE_URL
import com.asssignment.dailyround.features.module_section.data.datasource.ModuleRemoteDataSource
import com.asssignment.dailyround.features.quiz.data.datasource.QuizRemoteDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun provideRetrofitInstance(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        ).client(OkHttpClient())
        .build()

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideQuizRemoteDataSource(retrofit: Retrofit): QuizRemoteDataSource {
        return retrofit.create(QuizRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideModuleRemoteDataSource(retrofit: Retrofit) : ModuleRemoteDataSource {
        return retrofit.create(ModuleRemoteDataSource::class.java)
    }
}