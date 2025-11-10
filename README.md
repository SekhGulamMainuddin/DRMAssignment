## DailyRound Assignment 
Please install assignment-app.apk for experiencing the app. Its present in the root directory.

1. Followed MVVM architecture pattern.
2. Used Retrofit for network calls.
3. Implemented Room database for local data storage.
4. Used Flows and Coroutines for asynchronous programming.
5. Implemented Dependency Injection using Hilt.
6. Used Compose UI

# Folder Structure (Please open this in Android Studio Directly)
app/src/main/java/com.assignment.dailyround
├── core // It includes the MainActivity and Application Class as well
│   ├── components // Common Compose UI components
│   ├── di // Dependency Injection modules including Network, Database, Repository modules
│   ├── local_db // Room database related classes
│   ├── navigation // Navigation related classes
│   ├── theme // Theme related classes
├── features
│   ├── home // Home feature module
│   │   ├── data // Data layer for Home feature
│   │   ├── domain // Domain layer for Home feature
│   │   ├── presentation // Presentation layer for Home feature
│   ├── quiz // Quiz feature module
│   │   ├── data // Data layer for Quiz feature
│   │   ├── domain // Domain layer for Quiz feature
│   │   ├── presentation // Presentation layer for Quiz feature
│   ├── result // Result feature module
│   │   ├── presentation // Presentation layer for Result feature

## Approach

1. Home Screen
    1. Displays the overall result of all taken tests like highest streak, total quiz taken and last quiz highest streak.
    2. If any Quiz is left in between, the user will get a button to continue with the last test.

2. Quiz Screen
    1. It first fetches the questions from the Remote API and then starts the quiz.
    2. If it’s a continue quiz, it starts from the last attempted question, otherwise starts a fresh new quiz.
    3. This is the model used to store the quiz results locally. For every selected option, the result is saved:

        ```kotlin
        @Entity
        data class QuizResultEntity(
            @PrimaryKey
            val id: String,
            val currentQuestionId: Int,
            val correctAnswered: List<Int>,
            val wrongAnswered: List<Int>,
            val skippedQuestions: List<Int>,
            val currentStreak: Int,
            val highestStreak: Int,
            val completedTime: Long? = null,
            val lastUpdatedTime: Long = System.currentTimeMillis(),
        )
        ```

3. Answer Submission
    1. The `submitAnswer` function handles submitting the selected option for a question.
    2. If no option is selected, the question is marked skipped and current streak reset to 0.
    3. If the answer is correct, it’s added to correctAnswered, progress is updated, and streak increased.
    4. If the answer is wrong, it’s added to wrongAnswered, progress updated, and streak reset.
    5. Updates the local quiz result in Room with current/last question, streaks, and completed time if last question.
    6. UI state is updated: shows submitting, then correct/wrong/skipped feedback.
    7. Moves to next question automatically after 2 seconds, or marks quiz completed if it was the last question.
    8. Handles errors and shows submit error if updating the result fails.

   ```kotlin
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

           quizRepository.updateQuizResult(newQuizResult).onSuccess {
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
    ```
4. Quiz Question UI
    1. `QuizQuestionComponent` displays a single quiz question along with options, progress, and action buttons.
    2. Shows the current question number and total questions at the top.
    3. Displays the question text in bold and slightly larger font.
    4. Lists all options in a column with spacing between them:
        - Each option is clickable if not already answered.
        - Options are bordered with different colors depending on state:
            - Default border when unanswered.
            - Green border for correct answer.
            - Red border for wrong answer.
        - If an option is selected and result is known, shows a Check or Close icon.
    5. `QuestionsResultRow` shows a small summary of all questions’ progress at the top:
        - Correct → green check icon
        - Wrong → red close icon
        - Attempting → gray filled circle
        - Not Attempted → light gray border circle
        - Skipped → thicker light gray border circle
    6. Bottom buttons:
        - "Skip Question" → submits null as answer
        - "Exit Quiz" → calls the exit callback
    7. Layout spacing:
        - Uses `Column` with `Arrangement.spacedBy(16.dp)` for vertical spacing
        - Spacer with `weight(1f)` pushes buttons to the bottom
