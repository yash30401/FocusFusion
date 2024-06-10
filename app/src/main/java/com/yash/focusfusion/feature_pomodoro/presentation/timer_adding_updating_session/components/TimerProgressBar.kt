package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerProgressBar(
    timeLeft: Long,
    totalTime: Long,
    modifier: Modifier = Modifier,
) {

    var animationPlayed by remember {
        mutableStateOf(true)
    }

    val percentage by remember {
        mutableStateOf((timeLeft * 100) / totalTime)
    }
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0
        )
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier
        .padding(8.dp)
        .clickable {

        }) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFFFAF9FD),
                        Color(0xFFBC9FF1)
                    ),
                ),
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimerCircularBarPreview() {
    TimerProgressBar(1500, 1500, )
}