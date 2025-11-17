package com.asssignment.dailyround.features.module_section.data.models

import com.google.gson.annotations.SerializedName

data class ModuleResponseModel(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("questions_url")
    val questionsUrl: String
)
