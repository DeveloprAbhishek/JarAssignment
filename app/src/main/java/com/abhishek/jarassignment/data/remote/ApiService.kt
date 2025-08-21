package com.abhishek.jarassignment.data.remote

import com.abhishek.jarassignment.data.model.OnboardingResponse
import com.abhishek.jarassignment.utils.AppConstants.GET_ONBOARDING_DATA
import retrofit2.http.GET

interface ApiService {
    @GET(GET_ONBOARDING_DATA)
    suspend fun getOnboardingData(): OnboardingResponse
}

