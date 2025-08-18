package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components

import android.content.res.Configuration
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerSharedViewModel
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import java.util.concurrent.TimeUnit
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TimerProgressBar(
    timerSharedViewModel: TimerSharedViewModel,
    strokeWidth: Dp = 4.dp,
    modifier: Modifier = Modifier,
    onTaskTagChanged: (TaskTag) -> Unit,
) {

    val timeInMinutes by timerSharedViewModel.initialFocusTime.collectAsState()
    Log.d("PROGRESSSTARTTIME", "Progress component:- ${timeInMinutes.toString()}")
    val timeLeft by timerSharedViewModel.timeLeft.collectAsState()
    Log.d("PROGRESSSTARTTIME", "Time Left component:- ${timeLeft.toString()}")
    val workState by timerSharedViewModel.workState.collectAsState()
    val isTimerRunning by timerSharedViewModel.isRunning.collectAsState()

    var taskTag by remember {
        mutableStateOf(workState)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val progress = remember(timeLeft) {
        derivedStateOf {
            // Ensure progress is between 0.0 and 1.0
            val progressValue =
                1f - (timeInMinutes * 60 - TimeUnit.MILLISECONDS.toSeconds(timeLeft)) / (timeInMinutes * 60f)
            maxOf(0f, minOf(1f, progressValue))
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress.value,
        animationSpec = tween(durationMillis = 1000)
    )

    Log.d("TimerProgressBar", "animatedProgress: $animatedProgress")
    Log.d("TimerProgressBar", "timeLeft: $timeLeft")

    val arcColor = MaterialTheme.colorScheme.secondary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val centerBackgroundColor = MaterialTheme.colorScheme.surface
    val centerBorderColor = MaterialTheme.colorScheme.background
    val primaryTextColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val accentColor = MaterialTheme.colorScheme.primary

    if (showDialog) {
        TaskTagEditDialog(setShowDialog = {
            showDialog = it
        }) { newTaskTagValue ->
            onTaskTagChanged(newTaskTagValue)

            if (newTaskTagValue == TaskTag.WORK) {
                taskTag = TaskTag.WORK
            } else if (newTaskTagValue == TaskTag.STUDY) {
                taskTag = TaskTag.STUDY
            } else if (newTaskTagValue == TaskTag.SPORT) {
                taskTag = TaskTag.SPORT
            } else if (newTaskTagValue == TaskTag.RELAX) {
                taskTag = TaskTag.RELAX
            } else if (newTaskTagValue == TaskTag.ENTERTAINMENT) {
                taskTag = TaskTag.ENTERTAINMENT
            } else if (newTaskTagValue == TaskTag.EXERCISE) {
                taskTag = TaskTag.EXERCISE
            } else if (newTaskTagValue == TaskTag.SOCIAL) {
                taskTag = TaskTag.SOCIAL
            } else if (newTaskTagValue == TaskTag.OTHER) {
                taskTag = TaskTag.OTHER
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .padding(8.dp)

    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            Brush.sweepGradient(
                0.0f to Color(0xFFBC9FF1), // Dark color
                1.0f to Color(0xFFFAF9FD)  // Light color
            )
            drawArc(
                color = arcColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360 * (1f - animatedProgress),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            if (timeLeft < TimeUnit.MINUTES.toMillis(timeInMinutes.toLong())) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = ((360 * (1f - animatedProgress)) - 90f) * (PI / 180f).toFloat()
                val radius = size.width / 2f
                val x = center.x + cos(beta) * radius
                val y = center.y + sin(beta) * radius

                // Draw the circle at the tip of the arc
                drawPoints(
                    points = listOf(Offset(x, y)),
                    pointMode = PointMode.Points,
                    color = arcColor,
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
                    shape = CircleShape)
                .clip(CircleShape)
                .size(205.dp)
                .background(centerBackgroundColor)
                .border(width = 3.dp, color = centerBorderColor, shape = CircleShape)

        ) {

            Text(
                text = String.format("%02d:%02d", timeInMinutes, timeInMinutes / 60),
                fontSize = 20.sp,
                color = secondaryTextColor,
                fontFamily = FontFamily(Font(R.font.baloo_bhaijan_semi_bold))
            )
            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            Text(
                text = if (TimeUnit.MILLISECONDS.toMinutes(timeLeft).toInt() != 25) String.format(
                    "%02d:%02d",
                    minutes,
                    seconds
                ) else
                    String.format("%02d:%02d", timeInMinutes, timeInMinutes / 60),
                fontSize = 48.sp,
                color = primaryTextColor,
                fontFamily = FontFamily(Font(R.font.baloo_bold))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.width(90.dp)
            ) {

                Text(
                    text = taskTag.name.toLowerCase().replaceFirstChar { it.uppercase() },
                    color = accentColor,
                    fontSize = if (taskTag.name.length > 6) 18.sp else 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = if (!isTimerRunning) Modifier.clickable {
                        showDialog = true
                    } else Modifier.clickable {}
                )
                IconButton(onClick = {
                    if (!isTimerRunning) {
                        showDialog = true
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_edit_24),
                        contentDescription = "Edit TaskTag",
                        tint = accentColor
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerCircularBarPreview() {

    val timerSharedViewModel = TimerSharedViewModel()
    FocusFusionTheme {
        TimerProgressBar(
            timerSharedViewModel = timerSharedViewModel
        ) {}
    }
}