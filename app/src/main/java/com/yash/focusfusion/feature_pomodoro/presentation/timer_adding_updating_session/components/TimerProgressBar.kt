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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TimerProgressBar(
    timeInMinutes: Int,
    strokeWidth: Dp = 4.dp,
    modifier: Modifier = Modifier
) {

    var timeLeft by remember {
        mutableIntStateOf(timeInMinutes * 60) // 25 minutes in seconds
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    var isTimerStarted by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isTimerRunning = false
        }
    }

    val progress by remember(timeLeft) {
        derivedStateOf {
            timeLeft / (timeInMinutes * 60f)
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier
        .padding(8.dp)
        .clickable {
            isTimerRunning = !isTimerRunning
            isTimerStarted = true
        }) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val gradient = Brush.sweepGradient(
                0.0f to Color(0xFFBC9FF1), // Dark color
                1.0f to Color(0xFFFAF9FD)  // Light color
            )
            drawArc(
                color = Color(0xFFBC9FF1),
                startAngle = -90f,
                sweepAngle = -360 * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            if (isTimerStarted) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (-360f * animatedProgress - 90f) * (PI / 180f).toFloat()
                val radius = size.width / 2f
                val x = center.x + cos(beta) * radius
                val y = center.y + sin(beta) * radius

                // Draw the circle at the tip of the arc
                drawPoints(
                    points = listOf(Offset(x, y)),
                    pointMode = PointMode.Points,
                    color = Color(0xFFBC9FF1),
                    strokeWidth = (strokeWidth * 3f).toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Study",
                color = Color(0xFFFF6347),
                fontSize = 20.sp
            )
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimerCircularBarPreview() {
    TimerProgressBar(1)
}