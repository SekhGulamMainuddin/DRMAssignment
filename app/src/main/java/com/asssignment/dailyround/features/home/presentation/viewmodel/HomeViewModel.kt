package com.asssignment.dailyround.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asssignment.dailyround.features.home.domain.repository.HomeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    homeRepository: HomeRepository,
    @Assisted val moduleId: String,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(moduleId: String): HomeViewModel
    }

    val totalNumbersOfQuizTaken = homeRepository.getTotalNumberOfQuizzesTaken(moduleId).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    val longestStreak = homeRepository.getLongestStreak(moduleId).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    val lastQuizStreak = homeRepository.getLastQuizStreak(moduleId).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    val lastQuizResult = homeRepository.getLastQuizResult(moduleId)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )
}