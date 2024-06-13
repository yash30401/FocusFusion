package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.ui.theme.fontFamily
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TimerProgressBar(
    timeInMinutes: Int,
    isTimerRunning: Boolean,
    isTimerStarted: Boolean,
    strokeWidth: Dp = 4.dp,
    modifier: Modifier = Modifier,
    onTimeUp: () -> Unit,
) {

    var timeLeft by remember {
        mutableIntStateOf(timeInMinutes * 60) // 25 minutes in seconds
    }


    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            onTimeUp()
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

    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .padding(8.dp)
            .background(Color(0xFFFFFDFC))
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            Brush.sweepGradient(
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    spotColor = Color(0xFF5F14E7)
                )
                .clip(CircleShape)
                .size(205.dp)
                .background(Color(0xFFF8F8F8))
                .border(width = 3.dp, color = Color.White, shape = CircleShape)

        ) {

            Text(
                text = String.format("%02d:%02d", timeInMinutes, timeInMinutes / 60),
                fontSize = 20.sp,
                color = Color(0xFFB5B5B5),
                fontFamily = FontFamily(Font(R.font.baloo_bhaijan_semi_bold))
            )
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                fontSize = 48.sp,
                color = Color(0xFF4D4D4D),
                fontFamily = FontFamily(Font(R.font.baloo_bold))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.width(90.dp)
            ) {
                Text(
                    text = "Study",
                    color = Color(0xFFFF8D61),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}


@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TimerCircularBarPreview() {

    TimerProgressBar(25, false, false) {}

}