package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yash.focusfusion.R

@Composable
fun AnimatedPreloaderLottie(modifier: Modifier = Modifier) {
    val preLoaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.on_boarding_app_icon_anim
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preLoaderLottieComposition,
        iterations = 1,
        isPlaying = true
    )

    LottieAnimation(
        composition = preLoaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}