package com.asssignment.dailyround.core.di

import android.content.Context
import androidx.room.Room
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
    ).build()

    @Provides
    @Singleton
    fun provideHomeLocalDataSource(db: LocalDatabase) = db.getHomeLocalDataSource()

    @Provides
    @Singleton
    fun provideQuizLocalDataSource(db: LocalDatabase) = db.getQuizLocalDataSource()
}