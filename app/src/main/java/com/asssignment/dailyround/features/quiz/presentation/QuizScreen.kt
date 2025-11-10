package com.asssignment.dailyround.features.quiz.presentation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextBodySmall
import com.asssignment.dailyround.core.components.AppTextTitleSmall
import com.asssignment.dailyround.core.components.PrimaryButton
import com.asssignment.dailyround.core.components.SecondaryButton
import com.asssignment.dailyround.core.navigation.NavigationHelper
import com.asssignment.dailyround.features.quiz.presentation.components.MessageAndOrRetry
import com.asssignment.dailyround.features.quiz.presentation.components.QuizQuestionComponent
import com.asssignment.dailyround.features.quiz.presentation.viewmodel.QuizDialogUiState
import com.asssignment.dailyround.features.quiz.presentation.viewmodel.QuizUiState
import com.asssignment.dailyround.features.quiz.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(navController: NavController, quizId: String?) {
    val viewModel = hiltViewModel<QuizViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState by viewModel.uiDialogState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getQuizQuestions()
    }

    if (dialogState !is QuizDialogUiState.Hidden && dialogState !is QuizDialogUiState.ExitConfirmation) {
        LaunchedEffect(dialogState) {
            when (dialogState) {
                is QuizDialogUiState.QuestionsLoaded -> {
                    if (quizId != null) {
                        viewModel.startPendingQuiz(quizId)
                    } else {
                        viewModel.startNewQuiz()
                    }
                }

                is QuizDialogUiState.QuizCompleted -> {
                    val quizResult = (dialogState as QuizDialogUiState.QuizCompleted).quizResult
                    navController.navigate(
                        NavigationHelper.ResultsRoute.createRoute(
                            quizResult.highestStreak,
                            quizResult.correctAnswered.size,
                            quizResult.skippedQuestions.size + quizResult.wrongAnswered.size + quizResult.correctAnswered.size,
                            quizResult.skippedQuestions.size,
                        )
                    ) {
                        popUpTo(NavigationHelper.QuizRoute.routeName) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                is QuizDialogUiState.QuizQuestionSubmitError -> {
                    Toast.makeText(
                        context,
                        (dialogState as QuizDialogUiState.QuizQuestionSubmitError).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is QuizDialogUiState.ExitScreen -> {
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    } else if (dialogState is QuizDialogUiState.ExitConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
            title = {
                AppTextTitleSmall(
                    text = stringResource(R.string.exit_quiz),
                )
            },
            text = {
                AppTextBodySmall(
                    text = stringResource(R.string.are_you_sure_you_want_to_exit_the_quiz)
                )
            },
            confirmButton = {
                PrimaryButton(
                    buttonText = stringResource(R.string.yes),
                    onClick = {
                        viewModel.exitQuiz()
                    }
                )
            },
            dismissButton = {
                SecondaryButton(
                    buttonText = stringResource(R.string.no),
                    onClick = {
                        viewModel.hideDialog()
                    }
                )
            }
        )
    }

    BackHandler(enabled = true) {
        viewModel.showExitConfirmationDialog()
    }

    Scaffold { paddingValues ->
        when (uiState) {
            is QuizUiState.QuestionsLoadError -> MessageAndOrRetry(
                modifier = Modifier
                    .padding(paddingValues),
                message = (uiState as QuizUiState.QuestionsLoadError).message,
            ) {
                viewModel.getQuizQuestions()
            }

            is QuizUiState.QuizStartError -> MessageAndOrRetry(
                modifier = Modifier
                    .padding(paddingValues),
                message = (uiState as QuizUiState.QuizStartError).message,
            ) {
                if (quizId != null) {
                    viewModel.startPendingQuiz(quizId)
                } else {
                    viewModel.startNewQuiz()
                }
            }

            is QuizUiState.QuizInProgress -> QuizQuestionComponent(
                modifier = Modifier
                    .padding(paddingValues),
                uiState = uiState as QuizUiState.QuizInProgress,
                onOptionSelected = { optionIndex ->
                    val currentQuestionIndex =
                        (uiState as QuizUiState.QuizInProgress).currentQuestionIndex
                    val question = (uiState as QuizUiState.QuizInProgress).question
                    viewModel.submitAnswer(optionIndex, currentQuestionIndex, question)
                },
                onExitClicked = {
                    viewModel.showExitConfirmationDialog()
                },
            )

            else -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp
                )
            }
        }
    }
}



