package com.asssignment.dailyround.features.module_section.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModuleEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val moduleName: String,
    val moduleDesc: String,
    val lastQuizId: String? = null,
    val isLastQuizCompleted: Boolean = false,
    val totalNumberOfQuestions: Int = 0,
    val correctAnswered: Int = 0,
)