package com.asssignment.dailyround.features.quiz.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity

@Dao
interface QuizLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: QuizResultEntity)

    @Update
    suspend fun update(quiz: QuizResultEntity)

    @Query("select * from quizresultentity where id = :id limit 1")
    suspend fun getQuiz(id: String) : QuizResultEntity
}