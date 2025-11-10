package com.asssignment.dailyround.features.quiz.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizResultEntity(
    @PrimaryKey()
    val id: String,
    val currentQuestionId: Int,
    val correctAnswered: List<Int>,
    val wrongAnswered: List<Int>,
    val skippedQuestions: List<Int>,
    val currentStreak: Int,
    val highestStreak: Int,
    val completedTime : Long? = null,
    val lastUpdatedTime : Long = System.currentTimeMillis(),
)