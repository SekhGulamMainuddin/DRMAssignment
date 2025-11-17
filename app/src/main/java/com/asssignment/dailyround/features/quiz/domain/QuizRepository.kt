package com.asssignment.dailyround.features.quiz.domain

import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion

interface QuizRepository {
    suspend fun getQuizQuestions(path: String): Result<List<QuizQuestion>>
    suspend fun createNewQuiz(quizResult: QuizResultEntity) : Result<Unit>
    suspend fun updateQuizResult(quizResult: QuizResultEntity) : Result<Unit>
    suspend fun loadPendingQuiz(id: String): Result<QuizResultEntity>
}