package com.abhishek.jarassignment.di

import com.abhishek.jarassignment.data.remote.ApiService
import com.abhishek.jarassignment.data.repository.OnboardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOnboardingRepository(apiService: ApiService): OnboardingRepository {
        return OnboardingRepository(apiService)
    }
}

