package com.abhishek.jarassignment.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.abhishek.jarassignment.R
import com.abhishek.jarassignment.data.model.EducationCard
import com.abhishek.jarassignment.data.model.SaveButtonCta
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun OnboardingCardsListComposable(
    modifier: Modifier,
    items: List<EducationCard>,
    expandedIndex: Int,
    onCardExpanded: (Int) -> Unit,
    backgroundBrush: Brush,
    button: SaveButtonCta,
    lottieUrl: String,
    initialAnimationFinished: Boolean,
    onInitialAnimationFinished: () -> Unit,
    onBackgroundUpdate: (Int) -> Unit,
    onBottomButtonClick: () -> Unit
) {
    val listState = rememberLazyListState()
    var currentItemIndex by remember { mutableIntStateOf(0) }
    var translatingItemIndex by remember { mutableIntStateOf(-1) }
    var revertTiltForIndex by remember { mutableIntStateOf(-1) }
    val buttonAlpha by animateFloatAsState(
        targetValue = if (initialAnimationFinished) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(items, key = { _, it -> it.expandStateText }) { index, item ->
                OnboardingCardComposable(
                    title = item.expandStateText,
                    imageUrl = item.image,
                    cardColor = try {
                        Color(item.backGroundColor.toColorInt())
                    } catch (e: Exception) {
                        colorResource(id = R.color.dark_gray)
                    },
                    index = index,
                    currentItemIndex = currentItemIndex,
                    lastIndex = index == items.lastIndex,
                    listState = listState,
                    onCurrentItemVisible = { currentItemIndex++ },
                    expandedIndex = expandedIndex,
                    onCardClick = {
                        if (initialAnimationFinished) {
                            if (expandedIndex != index) {
                                onCardExpanded(index)
                            }
                        } else {
                            onCardExpanded(if (expandedIndex == index) -1 else index)
                        }
                    },
                    translatingItemIndex = translatingItemIndex,
                    onItemTranslationStart = {
                        translatingItemIndex = it
                        onCardExpanded(it)
                    },
                    revertTiltForIndex = revertTiltForIndex,
                    onRevertTilt = { revertTiltForIndex = it },
                    onLastItemAnimationEnd = onInitialAnimationFinished,
                    initialAnimationFinished = initialAnimationFinished,
                    onBackgroundUpdate = onBackgroundUpdate
                )
            }
        }

        Button(
            onClick = {
                onBottomButtonClick()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .graphicsLayer {
                    alpha = buttonAlpha
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = button.text,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(button.textColor.toColorInt())
                )
            )
            val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}