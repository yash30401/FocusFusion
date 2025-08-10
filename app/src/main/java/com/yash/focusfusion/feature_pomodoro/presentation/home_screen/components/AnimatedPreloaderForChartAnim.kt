package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yash.focusfusion.R

@Composable
fun AnimatedPreloaderForChartAnim(modifier: Modifier = Modifier) {
    val preLoaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.chart_anim
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preLoaderLottieComposition,
        iterations = Int.MAX_VALUE,
        isPlaying = true,
        restartOnPlay = true
    )

    LottieAnimation(
        composition = preLoaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}