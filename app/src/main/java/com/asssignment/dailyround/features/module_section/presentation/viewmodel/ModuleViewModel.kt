package com.asssignment.dailyround.features.module_section.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asssignment.dailyround.features.module_section.data.models.ModuleResponseModel
import com.asssignment.dailyround.features.module_section.domain.ModuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModuleViewModel @Inject constructor(private val repository: ModuleRepository) : ViewModel() {
    private val _moduleUIState = MutableStateFlow<ModuleUIState>(ModuleUIState.Loading)
    val moduleUIState: StateFlow<ModuleUIState>
        get() = _moduleUIState

    val tempCachedRemoteModules = mutableListOf<ModuleResponseModel>()

    fun loadModules() = viewModelScope.launch(Dispatchers.IO) {
        if(!tempCachedRemoteModules.isEmpty()) {
            return@launch
        }

        _moduleUIState.emit(ModuleUIState.Loading)
        tempCachedRemoteModules.clear()

        repository.fetchModules().onSuccess {
            _moduleUIState.emit(ModuleUIState.LoadedState(it.first))
            tempCachedRemoteModules.addAll(it.second)
        }.onFailure {
            _moduleUIState.emit(ModuleUIState.ErrorState(it.message ?: "Something Went Wrong"))
        }
    }

    fun refreshModules() = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.refreshModules(tempCachedRemoteModules)

        result.onSuccess {
            _moduleUIState.emit(ModuleUIState.LoadedState(it))
        }
    }
}