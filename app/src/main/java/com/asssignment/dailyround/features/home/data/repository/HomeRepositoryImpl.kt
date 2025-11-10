package com.asssignment.dailyround.features.home.data.repository

import com.asssignment.dailyround.features.home.data.datasource.HomeLocalDataSource
import com.asssignment.dailyround.features.home.domain.repository.HomeRepository
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val localDataSource: HomeLocalDataSource
) : HomeRepository {

    override fun getTotalNumberOfQuizzesTaken(): Flow<Int> =
        localDataSource.getTotalNumberOfQuizzesTaken()

    override fun getLongestStreak(): Flow<Int> =
        localDataSource.getLongestStreak().map { it ?: 0 }

    override fun getLastQuizStreak(): Flow<Int> =
        localDataSource.getLastQuizStreak().map { it ?: 0 }

    override fun getLastQuizResult(): Flow<QuizResultEntity?> = localDataSource.getLastQuizResult()
}
