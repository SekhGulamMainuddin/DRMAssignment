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

    override fun getTotalNumberOfQuizzesTaken(moduleId: String): Flow<Int> =
        localDataSource.getTotalNumberOfQuizzesTaken(moduleId)

    override fun getLongestStreak(moduleId: String): Flow<Int> =
        localDataSource.getLongestStreak(moduleId).map { it ?: 0 }

    override fun getLastQuizStreak(moduleId: String): Flow<Int> =
        localDataSource.getLastQuizStreak(moduleId).map { it ?: 0 }

    override fun getLastQuizResult(moduleId: String): Flow<QuizResultEntity?> = localDataSource.getLastQuizResult(moduleId)
}
