package com.asssignment.dailyround.features.quiz.data.datasource

import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion
import retrofit2.Response
import retrofit2.http.GET

interface QuizRemoteDataSource {
    @GET(QuizApiConstants.QUIZ_QUESTIONS_ENDPOINT)
    suspend fun getQuizQuestions(): Response<List<QuizQuestion>>
}