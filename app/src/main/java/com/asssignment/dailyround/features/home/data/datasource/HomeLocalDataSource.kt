package com.asssignment.dailyround.features.home.data.datasource

import androidx.room.Dao
import androidx.room.Query
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeLocalDataSource {
    @Query("select count(*) from quizresultentity where moduleId=:moduleId")
    fun getTotalNumberOfQuizzesTaken(moduleId: String): Flow<Int>

    @Query("SELECT MAX(highestStreak) FROM quizresultentity where completedTime is not null and moduleId=:moduleId")
    fun getLongestStreak(moduleId: String): Flow<Int?>

    @Query("SELECT highestStreak FROM quizresultentity where completedTime is not null and moduleId=:moduleId ORDER BY completedTime DESC LIMIT 1")
    fun getLastQuizStreak(moduleId: String): Flow<Int?>

    @Query("select * from quizresultentity where completedTime IS NULL and moduleId=:moduleId order by lastUpdatedTime desc limit 1")
    fun getLastQuizResult(moduleId: String): Flow<QuizResultEntity?>
}