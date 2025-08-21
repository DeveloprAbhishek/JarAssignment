package com.abhishek.jarassignment.data.repository

import com.abhishek.jarassignment.data.model.OnboardingResponse
import com.abhishek.jarassignment.data.remote.ApiService
import javax.inject.Inject

class OnboardingRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getOnboardingData(): OnboardingResponse {
        return apiService.getOnboardingData()
    }
}
