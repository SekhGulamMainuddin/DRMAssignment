package com.asssignment.dailyround.features.quiz.data.models

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<Any?>,
    val correctOptionIndex: Int
)
