package com.asssignment.dailyround.features.home.domain.repository

import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTotalNumberOfQuizzesTaken(moduleId: String): Flow<Int>
    fun getLongestStreak(moduleId: String): Flow<Int>
    fun getLastQuizStreak(moduleId: String): Flow<Int>
    fun getLastQuizResult(moduleId: String): Flow<QuizResultEntity?>
}