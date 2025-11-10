package com.asssignment.dailyround.features.results.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextBodyMedium
import com.asssignment.dailyround.core.components.AppTextTitleLarge
import com.asssignment.dailyround.core.components.AppTextTitleMedium
import com.asssignment.dailyround.core.components.PrimaryButton
import com.asssignment.dailyround.core.navigation.NavigationHelper
import com.asssignment.dailyround.core.theme.DailyRoundAsssignmentTheme
import com.asssignment.dailyround.features.results.presentation.components.SummaryComponent
import com.asssignment.dailyround.features.results.presentation.viewmodel.ResultsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    highestStreak: Int,
    correctAns: Int,
    totalQuestions: Int,
    skippedQuestions: Int,
) {
    val viewModel = hiltViewModel<ResultsViewModel>()

    val longestStreak by viewModel.highestStream.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { AppTextTitleLarge(stringResource(R.string.quiz_results)) },
                actions = {
                    IconButton(
                        onClick = {
                            navController.popBackStack(
                                NavigationHelper.HomeRoute.routeName,
                                inclusive = false
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close Icon",
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextTitleMedium(stringResource(R.string.congratulations))
            AppTextBodyMedium(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 40.dp),
                text = stringResource(R.string.you_ve_completed_the_quiz_here_s_your_performance_summary),
                textAlign = TextAlign.Center
            )

            Row(Modifier.padding(top = 20.dp)) {
                SummaryComponent(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.correct_answers),
                    value = "$correctAns/$totalQuestions"
                )
                SummaryComponent(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.current_quiz_highest_streak),
                    value = "$highestStreak"
                )
            }
            Row(Modifier.padding(bottom = 20.dp)) {
                SummaryComponent(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.skipped_questions),
                    value = "$skippedQuestions"
                )
                SummaryComponent(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.highest_streak),
                    value = longestStreak.toString()
                )
            }

            PrimaryButton(
                buttonText = stringResource(R.string.restart_quiz),
                onClick = {
                    navController.navigate(
                        NavigationHelper.QuizRoute.createRoute(null)
                    ) {
                        popUpTo(NavigationHelper.ResultsRoute.routeName) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun ResultsScreenPreview() {
    DailyRoundAsssignmentTheme {
        ResultsScreen(
            navController = rememberNavController(),
            highestStreak = 5,
            correctAns = 8,
            totalQuestions = 10,
            skippedQuestions = 2,
        )
    }
}