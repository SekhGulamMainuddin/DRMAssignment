package com.asssignment.dailyround.core.di

import com.asssignment.dailyround.features.home.data.repository.HomeRepositoryImpl
import com.asssignment.dailyround.features.home.domain.repository.HomeRepository
import com.asssignment.dailyround.features.module_section.data.repository.ModuleRepositoryImpl
import com.asssignment.dailyround.features.module_section.domain.ModuleRepository
import com.asssignment.dailyround.features.quiz.data.repository.QuizRepositoryImpl
import com.asssignment.dailyround.features.quiz.domain.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        implementation: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        impl: QuizRepositoryImpl
    ): QuizRepository

    @Binds
    @Singleton
    abstract fun bindModuleRepository(
        impl: ModuleRepositoryImpl
    ): ModuleRepository
}