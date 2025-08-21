package com.abhishek.jarassignment.presentation.ui.components

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.abhishek.jarassignment.R
import com.abhishek.jarassignment.presentation.viewmodel.OnboardingUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreenComposable(
    modifier: Modifier,
    state: OnboardingUiState.Success,
    context: Activity,
    onBackPress: () -> Unit,
    onBottomButtonClick: () -> Unit
) {
    val data = state.data
    var expandedIndex by rememberSaveable { mutableIntStateOf(0) }
    var backgroundUpdateIndex by remember { mutableIntStateOf(0) }
    var initialAnimationFinished by remember { mutableStateOf(false) }

    var introVisible by remember { mutableStateOf(true) }
    var toolbarVisible by remember { mutableStateOf(false) }
    var cardsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500)
        introVisible = false
        toolbarVisible = true
        delay(500)
        cardsVisible = true
    }

    val introAlpha by animateFloatAsState(
        targetValue = if (introVisible) 1f else 0f,
        animationSpec = tween(800)
    )

    val toolbarAlpha by animateFloatAsState(
        targetValue = if (toolbarVisible) 1f else 0f,
        animationSpec = tween(500)
    )

    val backgroundCardIndex =
        if (!initialAnimationFinished) backgroundUpdateIndex else expandedIndex
    val currentCard = data.educationCardList.getOrNull(backgroundCardIndex)

    val cardStartColor = remember(currentCard) {
        currentCard?.let {
            try {
                Color(it.startGradient.toColorInt())
            } catch (e: Exception) {
                Color(0xFF713A65)
            }
        } ?: Color(0xFF713A65)
    }

    val cardEndColor = remember(currentCard) {
        currentCard?.let {
            try {
                Color(it.endGradient.toColorInt())
            } catch (e: Exception) {
                Color(0xFF713A65)
            }
        } ?: Color(0xFF713A65)
    }

    val introBackgroundColor = colorResource(id = R.color.dark_purple)

    val targetStartColor =
        if (!cardsVisible) introBackgroundColor else cardStartColor
    val targetEndColor =
        if (!cardsVisible) introBackgroundColor else cardEndColor

    val animatedStartColor by animateColorAsState(
        targetValue = targetStartColor,
        animationSpec = tween(500)
    )
    val animatedEndColor by animateColorAsState(
        targetValue = targetEndColor,
        animationSpec = tween(500)
    )

    val backgroundBrush = remember(animatedStartColor, animatedEndColor) {
        Brush.verticalGradient(
            colors = listOf(animatedStartColor, animatedEndColor)
        )
    }


    SideEffect {
        val window = context.window
        window.statusBarColor = animatedStartColor.toArgb()
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
    }

    Box(
        modifier = modifier
            .background(backgroundBrush)
    ) {
        IntroComposable(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = introAlpha },
            title = data.introTitle,
            subtitle = data.introSubtitle
        )

        Scaffold(
            topBar = {
                TopBarComposable(
                    toolbarText = data.toolBarText,
                    animatedStartColor = animatedStartColor,
                    toolbarAlpha = toolbarAlpha,
                    onBackPress = { onBackPress() }
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (cardsVisible) {
                    OnboardingCardsListComposable(
                        modifier = Modifier.fillMaxSize(),
                        items = data.educationCardList,
                        expandedIndex = expandedIndex,
                        onCardExpanded = { newIndex ->
                            expandedIndex = newIndex
                        },
                        backgroundBrush = backgroundBrush,
                        button = data.saveButtonCta,
                        lottieUrl = data.ctaLottie,
                        initialAnimationFinished = initialAnimationFinished,
                        onInitialAnimationFinished = {
                            initialAnimationFinished = true
                        },
                        onBackgroundUpdate = { index ->
                            backgroundUpdateIndex = index
                        },
                        onBottomButtonClick = { onBottomButtonClick() }
                    )
                }
            }
        }
    }
}
