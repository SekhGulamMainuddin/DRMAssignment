package com.asssignment.dailyround.features.results.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asssignment.dailyround.features.home.domain.repository.HomeRepository
import com.asssignment.dailyround.features.home.presentation.viewmodel.HomeViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel(
    assistedFactory = ResultsViewModel.Factory::class
)
class ResultsViewModel @AssistedInject constructor(
    homeRepository: HomeRepository,
    @Assisted val moduleId: String,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(moduleId: String): ResultsViewModel
    }

    val highestStream = homeRepository.getLongestStreak(moduleId).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )
}