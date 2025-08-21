package com.abhishek.jarassignment.presentation.viewmodel

import com.abhishek.jarassignment.data.model.ManualBuyEducationData

sealed interface OnboardingUiState {
    data object Loading : OnboardingUiState
    data class Success(val data: ManualBuyEducationData) : OnboardingUiState
    data class Error(val message: String) : OnboardingUiState
}
