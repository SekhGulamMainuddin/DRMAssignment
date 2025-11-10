package com.asssignment.dailyround.features.results.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asssignment.dailyround.features.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(homeRepository: HomeRepository) : ViewModel() {
    val highestStream = homeRepository.getLongestStreak().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )
}