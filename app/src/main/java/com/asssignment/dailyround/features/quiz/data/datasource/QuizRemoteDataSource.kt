package com.asssignment.dailyround.features.quiz.data.datasource

import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuizRemoteDataSource {
    @GET(QuizApiConstants.QUIZ_QUESTIONS_ENDPOINT)
    suspend fun getQuizQuestions(@Path("main_path") mainPath: String, @Path("question_path") path: String, @Path("file") file: String): Response<List<QuizQuestion>>
}