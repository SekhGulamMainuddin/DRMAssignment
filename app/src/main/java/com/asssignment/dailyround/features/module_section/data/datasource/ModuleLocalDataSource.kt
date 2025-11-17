package com.asssignment.dailyround.features.module_section.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.asssignment.dailyround.features.module_section.data.entities.ModuleEntity

@Dao
interface ModuleLocalDataSource {
    @Query("select * from moduleentity where id=:id")
    suspend fun getModule(id: String) : ModuleEntity

    @Update
    suspend fun updateModule(module: ModuleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertModules(modules: List<ModuleEntity>)

    @Query("select * from moduleentity where id in (:ids)")
    suspend fun getModules(ids: List<String>) : List<ModuleEntity>

    @Query("update moduleentity set lastQuizId=:qId, isLastQuizCompleted=:isCompleted, totalNumberOfQuestions = :tNQ, correctAnswered = :cNQ WHERE id = :moduleId")
    suspend fun updateModule(moduleId: String, qId: String, isCompleted: Boolean, tNQ: Int, cNQ: Int)
}