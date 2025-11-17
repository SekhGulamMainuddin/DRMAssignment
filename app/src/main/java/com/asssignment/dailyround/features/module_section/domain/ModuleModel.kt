package com.asssignment.dailyround.features.module_section.domain

import com.asssignment.dailyround.features.module_section.data.entities.ModuleEntity

data class ModuleModel(
    val id: String,
    val title: String,
    val description: String,
    val questionsUrl: String,
    val status: ModuleStatus,
    val correctAnswers: Int,
    val totalQuestions: Int,
)

enum class ModuleStatus {
    START,
    REVIEW,
    CONTINUE
}