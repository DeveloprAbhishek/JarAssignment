package com.abhishek.jarassignment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.jarassignment.presentation.ui.components.ErrorComposable
import com.abhishek.jarassignment.presentation.ui.components.LoadingComposable
import com.abhishek.jarassignment.presentation.ui.components.OnboardingScreenComposable
import com.abhishek.jarassignment.presentation.ui.theme.JarAssignmentTheme
import com.abhishek.jarassignment.presentation.viewmodel.OnboardingUiState
import com.abhishek.jarassignment.presentation.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JarAssignmentTheme {
                val viewModel: OnboardingViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                when (val state = uiState) {
                    is OnboardingUiState.Loading -> LoadingComposable(
                        modifier = Modifier.fillMaxSize()
                    )

                    is OnboardingUiState.Error -> ErrorComposable(
                        modifier = Modifier.fillMaxSize(),
                        errorMessage = state.message
                    )

                    is OnboardingUiState.Success -> {
                        OnboardingScreenComposable(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = state,
                            context = this,
                            onBackPress = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            onBottomButtonClick = {
                                onBottomButtonClick()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun onBottomButtonClick() {
        startActivity(Intent(this, LandingActivity::class.java))
    }
}
