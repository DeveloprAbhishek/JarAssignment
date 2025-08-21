package com.abhishek.jarassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.jarassignment.data.repository.OnboardingRepository
import com.abhishek.jarassignment.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Loading)
    val uiState: StateFlow<OnboardingUiState> = _uiState

    init {
        fetchOnboardingData()
    }

    private fun fetchOnboardingData() {
        viewModelScope.launch {
            _uiState.value = OnboardingUiState.Loading
            try {
                val response = repository.getOnboardingData()
                if (response.success) {
                    _uiState.value = OnboardingUiState.Success(response.data.manualBuyEducationData)
                } else {
                    _uiState.value = OnboardingUiState.Error(AppConstants.API_FAILED_RESPONSE)
                }
            } catch (e: Exception) {
                _uiState.value = OnboardingUiState.Error(e.message ?: AppConstants.UNKNOWN_ERROR)
            }
        }
    }
}
