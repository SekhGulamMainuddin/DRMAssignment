package com.asssignment.dailyround.features.module_section.domain

import com.asssignment.dailyround.features.module_section.data.models.ModuleResponseModel

interface ModuleRepository {
    suspend fun fetchModules() : Result<Pair<List<ModuleModel>, List<ModuleResponseModel>>>
    suspend fun refreshModules(list: List<ModuleResponseModel>) : Result<List<ModuleModel>>
    suspend fun updateModule(moduleId: String, qId: String, isCompleted: Boolean, tNQ: Int, cNQ: Int) : Result<Unit>
}