package com.asssignment.dailyround.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import com.asssignment.dailyround.features.quiz.data.models.QuizQuestion
import com.asssignment.dailyround.features.quiz.domain.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val quizUiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = quizUiState

    private val quizDialogUiState = MutableStateFlow<QuizDialogUiState>(QuizDialogUiState.Hidden)
    val uiDialogState: StateFlow<QuizDialogUiState> = quizDialogUiState

    private val questions = mutableListOf<QuizQuestion>()

    private lateinit var quizResultEntity: QuizResultEntity

    private val _questionsProgress = mutableListOf<QuestionsProgressState>()

    fun getQuizQuestions() = viewModelScope.launch(Dispatchers.IO) {
        quizUiState.value = QuizUiState.Loading

        quizRepository.getQuizQuestions().onSuccess {
            questions.clear()
            questions.addAll(it)
            _questionsProgress.clear()
            _questionsProgress.addAll(
                List(it.size) { QuestionsProgressState.NotAttempted })
            _questionsProgress[0] = QuestionsProgressState.Attempting
            quizUiState.value = QuizUiState.QuestionsLoaded(it)
            quizDialogUiState.value = QuizDialogUiState.QuestionsLoaded(it)
        }.onFailure { e ->
            quizUiState.value = QuizUiState.QuestionsLoadError(e.message ?: "Unknown Error")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun startNewQuiz() = viewModelScope.launch(Dispatchers.IO) {
        quizUiState.value = QuizUiState.Loading

        val question = questions.first()

        val newQuiz = QuizResultEntity(
            id = Uuid.random().toString(),
            currentStreak = 0,
            currentQuestionId = question.id,
            wrongAnswered = emptyList(),
            correctAnswered = emptyList(),
            completedTime = null,
            skippedQuestions = emptyList(),
            highestStreak = 0,
        )

        quizResultEntity = newQuiz

        quizRepository.createNewQuiz(newQuiz).onSuccess {
            quizUiState.value = QuizUiState.QuizInProgress(
                currentQuestionIndex = 0,
                question = question,
                totalNumberOfQuestions = questions.size,
                questionsProgress = _questionsProgress
            )
        }.onFailure { e ->
            quizUiState.value = QuizUiState.QuizStartError(e.message ?: "Unknown Error")
        }
    }

    fun startPendingQuiz(id: String) = viewModelScope.launch(Dispatchers.IO) {
        quizUiState.value = QuizUiState.Loading

        quizRepository.loadPendingQuiz(id).onSuccess { quizResult ->
            quizResultEntity = quizResult

            val currentQuestionIndex =
                questions.indexOfFirst { it.id == quizResult.currentQuestionId }

            _questionsProgress.clear()
            _questionsProgress.addAll(
                questions.map { question ->
                    if (question.id == quizResult.currentQuestionId) {
                        return@map QuestionsProgressState.Attempting
                    }
                    when {
                        quizResult.correctAnswered.contains(question.id) -> QuestionsProgressState.Correct
                        quizResult.wrongAnswered.contains(question.id) -> QuestionsProgressState.Wrong
                        quizResult.skippedQuestions.contains(question.id) -> QuestionsProgressState.Skipped
                        else -> QuestionsProgressState.NotAttempted
                    }
                }
            )

            quizUiState.value = QuizUiState.QuizInProgress(
                currentQuestionIndex = currentQuestionIndex,
                question = questions[currentQuestionIndex],
                totalNumberOfQuestions = questions.size,
                questionsProgress = _questionsProgress
            )
        }.onFailure { e ->
            quizUiState.value = QuizUiState.QuestionsLoadError(e.message ?: "Unknown Error")
        }
    }

    fun submitAnswer(optionIndex: Int?, currentQuestionIndex: Int, question: QuizQuestion) =
        viewModelScope.launch(Dispatchers.IO) {
            quizDialogUiState.value = QuizDialogUiState.QuizQuestionSubmitting

            val skippedList = quizResultEntity.skippedQuestions.toMutableList()
            val correctList = quizResultEntity.correctAnswered.toMutableList()
            val wrongList = quizResultEntity.wrongAnswered.toMutableList()

            val newQuestionProgressList = _questionsProgress.toMutableList()
            var lastStreak = quizResultEntity.currentStreak

            if (optionIndex == null) {
                skippedList.add(question.id)
                newQuestionProgressList[currentQuestionIndex] = QuestionsProgressState.Skipped
                lastStreak = 0
            } else if (question.correctOptionIndex == optionIndex) {
                correctList.add(question.id)
                newQuestionProgressList[currentQuestionIndex] = QuestionsProgressState.Correct
                lastStreak += 1
            } else {
                wrongList.add(question.id)
                newQuestionProgressList[currentQuestionIndex] = QuestionsProgressState.Wrong
                lastStreak = 0
            }

            val newQuizResult = quizResultEntity.copy(
                currentQuestionId = question.id,
                correctAnswered = correctList,
                wrongAnswered = wrongList,
                skippedQuestions = skippedList,
                completedTime = if (currentQuestionIndex == questions.lastIndex) {
                    System.currentTimeMillis()
                } else {
                    null
                },
                currentStreak = lastStreak,
                highestStreak = maxOf(quizResultEntity.highestStreak, lastStreak),
                lastUpdatedTime = System.currentTimeMillis()
            )

            quizRepository.updateQuizResult(
                newQuizResult
            ).onSuccess {
                hideDialog()

                quizUiState.value = QuizUiState.QuizInProgress(
                    currentQuestionIndex = currentQuestionIndex,
                    question = question,
                    totalNumberOfQuestions = questions.size,
                    questionsProgress = _questionsProgress,
                    correctOptionIndex = question.correctOptionIndex,
                    selectedOption = optionIndex,
                )

                delay(2000)

                if (currentQuestionIndex == questions.lastIndex) {
                    quizDialogUiState.value = QuizDialogUiState.QuizCompleted(newQuizResult)
                    quizUiState.value = QuizUiState.QuizCompleted(newQuizResult)
                } else {
                    val nextQuestion = questions[currentQuestionIndex + 1]
                    quizUiState.value = QuizUiState.QuizInProgress(
                        currentQuestionIndex = currentQuestionIndex + 1,
                        question = nextQuestion,
                        totalNumberOfQuestions = questions.size,
                        questionsProgress = newQuestionProgressList
                    )
                    newQuestionProgressList[currentQuestionIndex + 1] = QuestionsProgressState.Attempting
                }
                quizResultEntity = newQuizResult
                _questionsProgress.clear()
                _questionsProgress.addAll(newQuestionProgressList)
            }.onFailure { e ->
                quizDialogUiState.value =
                    QuizDialogUiState.QuizQuestionSubmitError(e.message ?: "Unknown Error")
                hideDialog()
            }
        }

    fun showExitConfirmationDialog() {
        quizDialogUiState.value = QuizDialogUiState.ExitConfirmation
    }

    fun exitQuiz() = viewModelScope.launch {
        quizDialogUiState.value = QuizDialogUiState.Hidden
        quizDialogUiState.value = QuizDialogUiState.ExitScreen
    }

    fun hideDialog() {
        quizDialogUiState.value = QuizDialogUiState.Hidden
    }
}