package com.asssignment.dailyround.core.navigation

import android.net.Uri

sealed class NavigationHelper(open val routeName: String) {
    data object ModuleListRoute : NavigationHelper("module_list_screen")

    data object HomeRoute : NavigationHelper("home_screen/?moduleId={moduleId}&questionUrl={questionUrl}") {
        fun createRoute(moduleId: String, questionUrl: String) : String = "home_screen/?moduleId=$moduleId&questionUrl=${Uri.encode(questionUrl)}"
    }

    data object QuizRoute : NavigationHelper("quiz_screen?quizId={quizId}&moduleId={moduleId}&questionUrl={questionUrl}") {
        fun createRoute(quizId: String?, moduleId: String, questionUrl: String): String = "quiz_screen?quizId=$quizId&moduleId=$moduleId&questionUrl=${Uri.encode(questionUrl)}"
    }

    data object ResultsRoute : NavigationHelper("results_screen?highestStreak={highestStreak}&correctAns={correctAns}&totalQuestions={totalQuestions}&skippedQuestions={skippedQuestions}&moduleId={moduleId}") {
        fun createRoute(highestStreak: Int, correctAns: Int, totalQuestions: Int, skippedQuestions: Int, moduleId: String): String {
            return "results_screen?highestStreak=$highestStreak&correctAns=$correctAns&totalQuestions=$totalQuestions&skippedQuestions=$skippedQuestions&moduleId=$moduleId"
        }
    }}
