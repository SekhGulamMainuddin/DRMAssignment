package com.asssignment.dailyround.features.quiz.data.repository

import com.asssignment.dailyround.features.module_section.data.datasource.ModuleLocalDataSource
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
    private val localDataSource: QuizLocalDataSource,
    private val moduleLocalDataSource: ModuleLocalDataSource,
) : QuizRepository {
    override suspend fun getQuizQuestions(path: String): Result<List<QuizQuestion>> {
        try {
            val pathData = splitGistRawPath(path)
            val response =
                remoteDataSource.getQuizQuestions(pathData.first, pathData.second, pathData.third)
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
            val module = moduleLocalDataSource.getModule(quizResult.moduleId)
            moduleLocalDataSource.updateModule(
                module.copy(
                    lastQuizId = quizResult.id,
                    isLastQuizCompleted = false,
                    totalNumberOfQuestions = 0,
                    correctAnswered = 0
                )
            )
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateQuizResult(quizResult: QuizResultEntity): Result<Unit> {
        try {
            localDataSource.update(quizResult)
            val module = moduleLocalDataSource.getModule(quizResult.moduleId)
            moduleLocalDataSource.updateModule(
                module.copy(
                    lastQuizId = quizResult.id,
                    isLastQuizCompleted = quizResult.completedTime != null,
                    correctAnswered = quizResult.correctAnswered.size,
                    totalNumberOfQuestions = quizResult.correctAnswered.size + quizResult.skippedQuestions.size + quizResult.wrongAnswered.size
                )
            )
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

    private fun splitGistRawPath(path: String): Triple<String, String, String> {
        // Split at "raw/" first
        val afterRaw = path.substringAfter("raw/")

        // Part 1: before raw
        val beforeRaw = path.substringBefore("/raw")

        // Now split the remaining part into two: id + filename
        val parts = afterRaw.split("/", limit = 2)
        val id = parts.getOrNull(0) ?: ""
        val file = parts.getOrNull(1) ?: ""

        return Triple(beforeRaw, id, file)
    }

}