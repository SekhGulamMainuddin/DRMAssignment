package com.asssignment.dailyround.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextBodyMedium
import com.asssignment.dailyround.core.components.AppTextBodySmall
import com.asssignment.dailyround.core.components.PrimaryButton
import com.asssignment.dailyround.core.components.SecondaryButton
import com.asssignment.dailyround.core.theme.CorrectGreen
import com.asssignment.dailyround.features.quiz.presentation.viewmodel.QuestionsProgressState
import com.asssignment.dailyround.features.quiz.presentation.viewmodel.QuizUiState
import kotlinx.coroutines.delay

@Composable
fun MessageAndOrRetry(
    modifier: Modifier = Modifier,
    message: String,
    onClickRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTextBodySmall(
            text = message
        )
        if (onClickRetry != null)
            SecondaryButton(
                modifier = Modifier.padding(top = 20.dp),
                buttonText = stringResource(R.string.retry),
                onClick = onClickRetry
            )
    }
}

@Composable
fun QuizQuestionComponent(
    modifier: Modifier = Modifier,
    uiState: QuizUiState.QuizInProgress,
    onOptionSelected: (Int?) -> Unit,
    onExitClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuestionsResultRow(
            questionsResults = uiState.questionsProgress,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(
                R.string.question,
                uiState.currentQuestionIndex + 1,
                uiState.totalNumberOfQuestions
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            text = uiState.question.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            uiState.question.options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (uiState.correctOptionIndex == null) MaterialTheme.colorScheme.secondary else if (uiState.correctOptionIndex == index) CorrectGreen else Color.Red
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (uiState.selectedOption == null) {
                                onOptionSelected(index)
                            }
                        }
                        .padding(16.dp)
                ) {
                    Row {
                        AppTextBodyMedium(modifier = Modifier.weight(1f), text = option)
                        if (uiState.correctOptionIndex != null && uiState.selectedOption == index) {
                            Icon(
                                if (uiState.correctOptionIndex == index) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = if (uiState.correctOptionIndex == index) "Correct" else "Wrong",
                                tint = if (uiState.correctOptionIndex == index) CorrectGreen else Color.Red,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column() {
            if(uiState.correctOptionIndex != null) {
                QuestionCountdown(questionId = uiState.question.id, isLastQuestion = uiState.currentQuestionIndex == uiState.totalNumberOfQuestions-1)
            }

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                buttonText = stringResource(R.string.skip_question),
                onClick = {
                    if (uiState.correctOptionIndex == null) {
                        onOptionSelected(null)
                    }
                },
            )
            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 8.dp),
                buttonText = stringResource(R.string.exit_quiz),
                onClick = onExitClicked,
            )
        }
    }
}


@Composable
fun QuestionsResultRow(
    questionsResults: List<QuestionsProgressState>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        questionsResults.forEach { result ->
            when (result) {
                QuestionsProgressState.Correct -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Correct",
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )
                }

                QuestionsProgressState.Wrong -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Wrong",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }

                QuestionsProgressState.Attempting -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Gray, shape = CircleShape)
                    )
                }

                QuestionsProgressState.NotAttempted -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(
                                border = BorderStroke(1.dp, color = Color.LightGray),
                                shape = CircleShape
                            )
                    )
                }

                QuestionsProgressState.Skipped -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(
                                border = BorderStroke(2.5.dp, color = Color.LightGray),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
@Composable
fun QuestionCountdown(
    modifier: Modifier = Modifier,
    questionId: Int,
    isLastQuestion: Boolean,
    totalTime: Int = 2,
) {
    var timeLeft by remember { mutableStateOf(totalTime.toFloat()) }

    LaunchedEffect(key1 = questionId) {
        timeLeft = totalTime.toFloat()
        while (timeLeft > 0f) {
            delay(500)
            timeLeft -= 0.5f
        }
        timeLeft = 0f
    }

    val progress = timeLeft / totalTime.toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(bottom = 14.dp),
    ) {
        AppTextBodyMedium(text = stringResource(if(isLastQuestion) R.string.moving_to_result_screen_in_s else R.string.moving_to_next_question_in_s, timeLeft.toInt()), modifier = Modifier.padding(bottom = 10.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
    }
}