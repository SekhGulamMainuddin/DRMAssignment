package com.asssignment.dailyround.core.navigation

sealed class NavigationHelper(open val routeName: String) {
    data object HomeRoute : NavigationHelper("home_screen")

    data object QuizRoute : NavigationHelper("quiz_screen/{quizId}") {
        fun createRoute(quizId: String?): String = "quiz_screen/$quizId"
    }

    data object ResultsRoute : NavigationHelper("results_screen?highestStreak={highestStreak}&correctAns={correctAns}&totalQuestions={totalQuestions}&skippedQuestions={skippedQuestions}") {
        fun createRoute(highestStreak: Int, correctAns: Int, totalQuestions: Int, skippedQuestions: Int): String {
            return "results_screen?highestStreak=$highestStreak&correctAns=$correctAns&totalQuestions=$totalQuestions&skippedQuestions=$skippedQuestions"
        }
    }}
