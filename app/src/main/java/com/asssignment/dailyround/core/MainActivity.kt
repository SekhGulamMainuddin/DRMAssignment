package com.asssignment.dailyround.core

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asssignment.dailyround.core.navigation.NavigationHelper
import com.asssignment.dailyround.core.theme.DailyRoundAsssignmentTheme
import com.asssignment.dailyround.features.home.presentation.ModuleHomeScreen
import com.asssignment.dailyround.features.module_section.presentation.ModuleScreen
import com.asssignment.dailyround.features.quiz.presentation.QuizScreen
import com.asssignment.dailyround.features.results.presentation.ResultsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyRoundAsssignmentTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    navController = navController,
                    startDestination = NavigationHelper.ModuleListRoute.routeName,
                ) {
                    composable(
                        route = NavigationHelper.ModuleListRoute.routeName
                    ) {
                        ModuleScreen(navController)
                    }

                    composable(
                        route = NavigationHelper.HomeRoute.routeName,
                        arguments = listOf(
                            navArgument("moduleId") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("questionUrl") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) {
                        val questionUrl = it.arguments?.getString("questionUrl").let {i-> Uri.decode(i) }

                        ModuleHomeScreen(navController, it.arguments?.getString("moduleId").orEmpty(), questionUrl)
                    }
                    composable(
                        route = NavigationHelper.QuizRoute.routeName,
                        arguments = listOf(
                            navArgument("quizId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            },
                            navArgument("questionUrl") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) { backStackEntry ->
                        val quizId = backStackEntry.arguments?.getString("quizId")
                        val moduleId = backStackEntry.arguments?.getString("moduleId").orEmpty()
                        val questionUrl = backStackEntry.arguments?.getString("questionUrl").let {i-> Uri.decode(i) }

                        Log.d("SEKH BRO", "onCreate: $questionUrl")

                        QuizScreen(navController, quizId, moduleId, questionUrl)
                    }
                    composable(
                        route = NavigationHelper.ResultsRoute.routeName,
                        arguments = listOf(
                            navArgument("highestStreak") {
                                type = NavType.IntType
                                defaultValue = 0
                            },
                            navArgument("correctAns") {
                                type = NavType.IntType
                                defaultValue = 0
                            },
                            navArgument("totalQuestions") {
                                type = NavType.IntType
                                defaultValue = 0
                            },
                            navArgument("skippedQuestions") {
                                type = NavType.IntType
                                defaultValue = 0
                            },
                        )
                    ) { backStackEntry ->
                        val highestStreak = backStackEntry.arguments?.getInt("highestStreak") ?: 0
                        val correctAns = backStackEntry.arguments?.getInt("correctAns") ?: 0
                        val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
                        val skippedQuestions = backStackEntry.arguments?.getInt("skippedQuestions") ?: 0
                        val moduleId = backStackEntry.arguments?.getString("moduleId").orEmpty()

                        ResultsScreen(
                            navController = navController,
                            highestStreak = highestStreak,
                            correctAns = correctAns,
                            totalQuestions = totalQuestions,
                            skippedQuestions = skippedQuestions,
                            moduleId = moduleId
                        )
                    }

                }
            }
        }
    }
}