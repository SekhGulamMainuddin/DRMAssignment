package com.asssignment.dailyround.core.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asssignment.dailyround.features.home.data.datasource.HomeLocalDataSource
import com.asssignment.dailyround.features.quiz.data.datasource.QuizLocalDataSource
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity

@Database(
    entities = [QuizResultEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(CustomTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getHomeLocalDataSource(): HomeLocalDataSource
    abstract fun getQuizLocalDataSource(): QuizLocalDataSource
}