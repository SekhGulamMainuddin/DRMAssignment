package com.asssignment.dailyround.features.home.data.datasource

import androidx.room.Dao
import androidx.room.Query
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeLocalDataSource {
    @Query("select count(*) from quizresultentity")
    fun getTotalNumberOfQuizzesTaken(): Flow<Int>

    @Query("SELECT MAX(highestStreak) FROM quizresultentity where completedTime is not null")
    fun getLongestWinningStreak(): Flow<Int?>

    @Query("SELECT highestStreak FROM quizresultentity where completedTime is not null ORDER BY completedTime DESC LIMIT 1")
    fun getLastQuizStreak(): Flow<Int?>

    @Query("select * from quizresultentity where completedTime IS NULL order by lastUpdatedTime desc limit 1")
    fun getLastQuizResult(): Flow<QuizResultEntity?>
}