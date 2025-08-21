package com.abhishek.jarassignment.presentation.ui.components

import android.graphics.Matrix
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil.compose.rememberAsyncImagePainter
import com.abhishek.jarassignment.R
import com.abhishek.jarassignment.utils.AppConstants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.tan

@OptIn(ExperimentalMotionApi::class)
@Composable
internal fun OnboardingCardComposable(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    cardColor: Color,
    index: Int,
    currentItemIndex: Int,
    lastIndex: Boolean,
    listState: LazyListState,
    onCurrentItemVisible: () -> Unit,
    expandedIndex: Int,
    onCardClick: () -> Unit,
    translatingItemIndex: Int,
    onItemTranslationStart: (Int) -> Unit,
    revertTiltForIndex: Int,
    onRevertTilt: (Int) -> Unit,
    onLastItemAnimationEnd: () -> Unit,
    initialAnimationFinished: Boolean,
    onBackgroundUpdate: (Int) -> Unit
) {
    val context = LocalContext.current
    val screenH = LocalConfiguration.current.screenHeightDp.dp
    val yOffset = remember { Animatable(screenH, Dp.VectorConverter) }
    val tiltAnim = remember { Animatable(0f) }

    var hasEntered by rememberSaveable { mutableStateOf(false) }
    val collapsed = if (!hasEntered) false else expandedIndex != index


    val customEasing = CubicBezierEasing(0.5f, 0.0f, 1.0f, 1.0f)

    val cardHeight by animateDpAsState(
        targetValue = if (collapsed) 80.dp else 400.dp,
        animationSpec = tween(durationMillis = 600, easing = customEasing), label = "cardHeight"
    )

    val progress by animateFloatAsState(
        targetValue = if (collapsed) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = customEasing),
        label = "motionProgress"
    )

    val isExpanded = progress < 0.5f

    val textStyle = if (isExpanded) {
        MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    } else {
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
    }

    val maxLines = if (isExpanded) Int.MAX_VALUE else 1

    val isVisible by remember {
        derivedStateOf { index in listState.layoutInfo.visibleItemsInfo.map { it.index } }
    }

    LaunchedEffect(currentItemIndex, hasEntered) {
        if (!hasEntered && currentItemIndex > index) {
            yOffset.snapTo(0.dp)
            hasEntered = true
        }
    }

    LaunchedEffect(currentItemIndex, isVisible, hasEntered) {
        if (isVisible && !hasEntered && index == currentItemIndex) {
            onItemTranslationStart(index)

            if (index > 0) {
                // Partial reveal and tilt
                coroutineScope {
                    launch {
                        yOffset.animateTo(
                            targetValue = 240.dp,
                            animationSpec = tween(AppConstants.ENTER_DURATION_MS / 2)
                        )
                    }
                    launch {
                        val skewDirection = if (index % 2 != 0) 5f else -5f
                        tiltAnim.animateTo(
                            targetValue = skewDirection,
                            animationSpec = tween(AppConstants.ENTER_DURATION_MS / 2)
                        )
                    }
                }

                // 1-second delay
                delay(1000)

                onBackgroundUpdate(index)

                // Full expansion
                onRevertTilt(index - 1)
                coroutineScope {
                    launch {
                        yOffset.animateTo(
                            targetValue = 0.dp,
                            animationSpec = tween(AppConstants.ENTER_DURATION_MS)
                        )
                    }
                    launch {
                        tiltAnim.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(AppConstants.ENTER_DURATION_MS)
                        )
                    }
                }
            } else {
                // Original animation for the first card
                delay(1000)
                onBackgroundUpdate(index)
                yOffset.animateTo(0.dp, animationSpec = tween(AppConstants.ENTER_DURATION_MS))
            }

            if (lastIndex) {
                delay(1000)
                onLastItemAnimationEnd()
            }

            hasEntered = true
            onCurrentItemVisible()
        }
    }

    LaunchedEffect(revertTiltForIndex) {
        if (index == revertTiltForIndex) {
            tiltAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(AppConstants.ENTER_DURATION_MS)
            )
        }
    }

    LaunchedEffect(key1 = collapsed) {
        if (collapsed) {
            if (!initialAnimationFinished) {
                val targetValue = if (translatingItemIndex == index + 1) {
                    if ((index + 1) % 2 != 0) -5f else 5f
                } else {
                    if (index % 2 != 0) 5f else -5f
                }
                tiltAnim.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(450)
                )
            }
        } else {
            tiltAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(600)
            )
        }
    }

    val motionScene = remember {
        context.resources.openRawResource(R.raw.expanded_card_scene)
            .readBytes().decodeToString()
    }

    val angle = tiltAnim.value
    val skewY = tan(angle * PI / 180).toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = yOffset.value)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawWithContent {
                drawIntoCanvas {
                    val matrix = Matrix()
                    val pivotX = if (angle > 0) 0f else size.width
                    val pivotY = size.height
                    matrix.postSkew(0f, skewY, pivotX, pivotY)
                    it.nativeCanvas.concat(matrix)
                    drawContent()
                }
            }
    ) {
        MotionLayout(
            motionScene = MotionScene(motionScene),
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = cardColor)
                    .height(cardHeight)
                    .layoutId("bg")
                    .clickable(
                        enabled = !(initialAnimationFinished && expandedIndex == index)
                    ) {
                        onCardClick()
                    }
            )

            Box(modifier = Modifier.layoutId("img")) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = 1f - progress }
                        .clip(RoundedCornerShape(10.dp))
                )
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = progress }
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Text(
                text = title,
                style = textStyle.copy(color = Color.White),
                modifier = Modifier
                    .layoutId("title")
                    .padding(horizontal = 16.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = maxLines
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.layoutId("arrow")
            )
        }
    }
}