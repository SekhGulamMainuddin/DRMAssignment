package com.asssignment.dailyround.features.module_section.data.datasource

import com.asssignment.dailyround.features.module_section.data.models.ModuleResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ModuleRemoteDataSource {
    @GET(ModuleApiConstants.GET_MODULES)
    suspend fun getModules() : Response<List<ModuleResponseModel>>
}