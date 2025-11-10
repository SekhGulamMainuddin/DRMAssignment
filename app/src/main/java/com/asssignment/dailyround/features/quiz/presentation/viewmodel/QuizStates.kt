package com.asssignment.dailyround.features.quiz.presentation.viewmodel

import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion

sealed interface QuizUiState {
    object Loading : QuizUiState
    data class QuestionsLoaded(val questions: List<QuizQuestion>) : QuizUiState
    data class QuestionsLoadError(val message: String) : QuizUiState
    data class QuizStartError(val message: String) : QuizUiState
    data class QuizInProgress(
        val currentQuestionIndex: Int,
        val totalNumberOfQuestions: Int,
        val question: QuizQuestion,
        val correctOptionIndex : Int? = null,
        val questionsProgress: List<QuestionsProgressState>,
        val selectedOption : Int? = null,
    ) : QuizUiState
    data class QuizCompleted(val quizResult: QuizResultEntity) : QuizUiState
}

sealed interface QuizDialogUiState {
    object Hidden : QuizDialogUiState
    object ExitConfirmation : QuizDialogUiState
    object ExitScreen : QuizDialogUiState
    data class QuestionsLoaded(val questions: List<QuizQuestion>) : QuizDialogUiState
    data class QuizCompleted(val quizResult: QuizResultEntity) : QuizDialogUiState
    data class QuizQuestionSubmitError(val message: String) : QuizDialogUiState
}

sealed interface QuestionsProgressState {
    object Attempting : QuestionsProgressState
    object NotAttempted : QuestionsProgressState
    object Correct : QuestionsProgressState
    object Wrong : QuestionsProgressState
    object Skipped : QuestionsProgressState
}