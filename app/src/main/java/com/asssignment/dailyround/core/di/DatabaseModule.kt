package com.asssignment.dailyround.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.asssignment.dailyround.core.local_db.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "daily_round_db",
    ).addMigrations(
        object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE QuizResultEntity ADD COLUMN moduleId TEXT NOT NULL DEFAULT ''"
                )
            }
        }
    ).build()

    @Provides
    @Singleton
    fun provideHomeLocalDataSource(db: LocalDatabase) = db.getHomeLocalDataSource()

    @Provides
    @Singleton
    fun provideQuizLocalDataSource(db: LocalDatabase) = db.getQuizLocalDataSource()

    @Provides
    @Singleton
    fun provideModuleLocalDataSource(db: LocalDatabase) = db.getModuleLocalDataSource()
}