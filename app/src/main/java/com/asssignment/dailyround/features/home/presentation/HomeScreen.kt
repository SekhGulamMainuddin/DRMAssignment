package com.asssignment.dailyround.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextBodySmall
import com.asssignment.dailyround.core.components.PrimaryButton
import com.asssignment.dailyround.core.components.SecondaryButton
import com.asssignment.dailyround.core.components.isPortrait
import com.asssignment.dailyround.core.navigation.NavigationHelper
import com.asssignment.dailyround.core.theme.CorrectGreen
import com.asssignment.dailyround.core.theme.DailyRoundAsssignmentTheme
import com.asssignment.dailyround.features.home.presentation.components.HighlightsBox
import com.asssignment.dailyround.features.home.presentation.components.HomeLottieAnim
import com.asssignment.dailyround.features.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val totalNumbersOfQuizTaken by viewModel.totalNumbersOfQuizTaken.collectAsStateWithLifecycle()
    val longestStreak by viewModel.longestStreak.collectAsStateWithLifecycle()
    val lastQuizStreak by viewModel.lastQuizStreak.collectAsStateWithLifecycle()
    val lastQuizResult by viewModel.lastQuizResult.collectAsStateWithLifecycle()

    val portrait = isPortrait()

    Scaffold(
        content = { paddingValues ->
            if (portrait) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 50.dp)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HighlightsSection(totalNumbersOfQuizTaken, longestStreak, lastQuizStreak)

                    HomeLottieAnim(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(vertical = 16.dp)
                    )

                    lastQuizResult?.let {
                        AppTextBodySmall(
                            modifier = Modifier.padding(top = 20.dp),
                            text = stringResource(R.string.incomplete_quiz_message)
                        )
                        SecondaryButton(
                            buttonText = "Continue Last Quiz",
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            navController.navigate(NavigationHelper.QuizRoute.createRoute(it.id))
                        }
                    } ?: Spacer(modifier = Modifier.height(70.dp))
                }
            } else {
                // Landscape: Row layout
                Row(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        HighlightsSection(totalNumbersOfQuizTaken, longestStreak, lastQuizStreak)

                        lastQuizResult?.let {
                            AppTextBodySmall(
                                text = stringResource(R.string.incomplete_quiz_message)
                            )
                            SecondaryButton(
                                buttonText = "Continue Last Quiz",
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                navController.navigate(NavigationHelper.QuizRoute.createRoute(it.id))
                            }
                        }
                    }

                    HomeLottieAnim(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .padding(vertical = 16.dp)
                    )
                }
            }
        },
        bottomBar = {
            PrimaryButton(
                buttonText = stringResource(R.string.start_new_quiz),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            ) {
                navController.navigate(NavigationHelper.QuizRoute.createRoute(null))
            }
        }
    )
}

@Composable
private fun HighlightsSection(
    totalNumbersOfQuizTaken: Int,
    longestStreak: Int,
    lastQuizStreak: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            HighlightsBox(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary,
                value = stringResource(
                    R.string.total_tests,
                    formatArgs = arrayOf(totalNumbersOfQuizTaken)
                )
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            HighlightsBox(
                modifier = Modifier.weight(1f),
                color = CorrectGreen,
                value = stringResource(
                    R.string.longest_streak,
                    formatArgs = arrayOf(longestStreak)
                )
            )
            HighlightsBox(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                value = stringResource(
                    R.string.last_streak,
                    formatArgs = arrayOf(lastQuizStreak)
                )
            )
        }
    }
}


@Preview
@Composable
private fun HomeScreenLight() {
    DailyRoundAsssignmentTheme(darkTheme = false) {
        HomeScreen(navController = rememberNavController())
    }
}


@Preview
@Composable
private fun HomeScreenDark() {
    DailyRoundAsssignmentTheme(darkTheme = true) {
        HomeScreen(navController = rememberNavController())
    }
}