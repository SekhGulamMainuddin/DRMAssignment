package com.asssignment.dailyround.features.module_section.presentation.viewmodel

import com.asssignment.dailyround.features.module_section.domain.ModuleModel

sealed interface ModuleUIState {
    object Loading: ModuleUIState
    data class LoadedState(val list: List<ModuleModel>) : ModuleUIState
    data class ErrorState(val message: String) : ModuleUIState
}