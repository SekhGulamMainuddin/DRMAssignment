package com.asssignment.dailyround.features.module_section.data.repository

import android.util.Log
import com.asssignment.dailyround.features.module_section.data.datasource.ModuleLocalDataSource
import com.asssignment.dailyround.features.module_section.data.datasource.ModuleRemoteDataSource
import com.asssignment.dailyround.features.module_section.data.entities.ModuleEntity
import com.asssignment.dailyround.features.module_section.data.models.ModuleResponseModel
import com.asssignment.dailyround.features.module_section.domain.ModuleModel
import com.asssignment.dailyround.features.module_section.domain.ModuleRepository
import com.asssignment.dailyround.features.module_section.domain.ModuleStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ModuleRemoteDataSource,
    private val localDataSource: ModuleLocalDataSource,
) : ModuleRepository {
    override suspend fun fetchModules(): Result<Pair<List<ModuleModel>, List<ModuleResponseModel>>> {
        return try {
            val result = remoteDataSource.getModules()
            if (!result.isSuccessful) {
                Result.failure(Exception(result.message()))
            } else if (result.body().isNullOrEmpty()) {
                Result.failure(Exception("Module is Empty"))
            } else {
                val modules = mutableListOf<ModuleEntity>()

                result.body()!!.forEach {
                    modules.add(
                        ModuleEntity(
                            id = it.id,
                            moduleName = it.title,
                            moduleDesc = it.description
                        )
                    )
                }
                localDataSource.upsertModules(modules)

                val resultList = _checkModulesStatusFromDB(result.body()!!)

                Result.success(Pair(resultList, result.body()!!))
            }
        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateModule(
        moduleId: String,
        qId: String,
        isCompleted: Boolean,
        tNQ: Int,
        cNQ: Int
    ): Result<Unit> {
        return try {
            localDataSource.updateModule(
                moduleId = moduleId,
                qId = qId,
                isCompleted = isCompleted,
                tNQ = tNQ,
                cNQ = cNQ
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshModules(list: List<ModuleResponseModel>): Result<List<ModuleModel>> {
        return try {
            val result = _checkModulesStatusFromDB(list)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun _checkModulesStatusFromDB(list: List<ModuleResponseModel>): List<ModuleModel> {
        val ids = list.map { it.id }.toSet()
        val localModules = localDataSource.getModules(ids.toList())
        Log.d("SEKH BRO", "_checkModulesStatusFromDB: $localModules")
        val moduleIdMap = hashMapOf<String, ModuleEntity>()

        localModules.forEach {
            moduleIdMap[it.id] = it
        }

        val resultList = mutableListOf<ModuleModel>()

        list.forEach {
            val moduleEntity = moduleIdMap[it.id]!!

            resultList.add(
                ModuleModel(
                    it.id,
                    it.title,
                    it.description,
                    getStringAfterRaw(it.questionsUrl),
                    if (moduleEntity.lastQuizId == null)
                        ModuleStatus.START
                    else if (moduleEntity.isLastQuizCompleted)
                        ModuleStatus.REVIEW
                    else
                        ModuleStatus.CONTINUE,
                    correctAnswers = moduleEntity.correctAnswered,
                    totalQuestions = moduleEntity.totalNumberOfQuestions
                ),
            )
        }

        return resultList
    }

    private fun getStringAfterRaw(url: String): String {
        return url.substringAfter("dr-samrat/")
    }
}