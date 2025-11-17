package com.asssignment.dailyround.core.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asssignment.dailyround.features.home.data.datasource.HomeLocalDataSource
import com.asssignment.dailyround.features.module_section.data.datasource.ModuleLocalDataSource
import com.asssignment.dailyround.features.module_section.data.entities.ModuleEntity
import com.asssignment.dailyround.features.quiz.data.datasource.QuizLocalDataSource
import com.asssignment.dailyround.features.quiz.data.entities.QuizResultEntity

@Database(
    entities = [QuizResultEntity::class, ModuleEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(CustomTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getHomeLocalDataSource(): HomeLocalDataSource
    abstract fun getQuizLocalDataSource(): QuizLocalDataSource
    abstract fun getModuleLocalDataSource() : ModuleLocalDataSource
}