package com.asssignment.dailyround.features.home.domain.repository

import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTotalNumberOfQuizzesTaken(): Flow<Int>
    fun getLongestStreak(): Flow<Int>
    fun getLastQuizStreak(): Flow<Int>
    fun getLastQuizResult(): Flow<QuizResultEntity?>
}