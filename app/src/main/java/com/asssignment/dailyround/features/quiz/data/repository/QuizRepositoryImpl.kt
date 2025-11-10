package com.asssignment.dailyround.features.quiz.data.repository

import android.util.Log
import com.asssignment.dailyround.features.quiz.data.datasource.QuizLocalDataSource
import com.asssignment.dailyround.features.quiz.data.datasource.QuizRemoteDataSource
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion
import com.asssignment.dailyround.features.quiz.domain.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource,
    private val localDataSource: QuizLocalDataSource
) : QuizRepository {
    override suspend fun getQuizQuestions(): Result<List<QuizQuestion>> {
        try {
            val response = remoteDataSource.getQuizQuestions()
            if (response.isSuccessful) {
                val questions = response.body() ?: emptyList()
                return Result.success(questions)
            } else {
                return Result.failure(Exception("Error fetching quiz questions: ${response.code()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    override suspend fun createNewQuiz(quizResult: QuizResultEntity): Result<Unit> {
        try {
            localDataSource.insert(quizResult)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateQuizResult(quizResult: QuizResultEntity): Result<Unit> {
        try {
            localDataSource.update(quizResult)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loadPendingQuiz(id: String): Result<QuizResultEntity> {
        return try {
            val pendingQuiz = localDataSource.getQuiz(id)
            Result.success(pendingQuiz)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exitQuiz(quizResult: QuizResultEntity) : Result<Unit>{
        return try {
            localDataSource.delete(quizResult)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}