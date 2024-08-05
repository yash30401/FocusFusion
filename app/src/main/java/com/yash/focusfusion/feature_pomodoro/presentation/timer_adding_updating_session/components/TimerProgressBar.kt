package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components

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
import androidx.compose.runtime.asLongState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerSharedViewModel
import com.yash.focusfusion.ui.theme.fontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import okhttp3.internal.concurrent.Task
import java.sql.Time
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TimerProgressBar(
    timeInMinutes: Int,
    timerSharedViewModel: TimerSharedViewModel,
    strokeWidth: Dp = 4.dp,
    modifier: Modifier = Modifier,
    onTaskTagChanged: (TaskTag) -> Unit,
) {


    val timeLeft by timerSharedViewModel.timeLeft.collectAsState()


    var taskTag by remember {
        mutableStateOf(TaskTag.STUDY)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }


    val progress = remember(timeLeft) {
        derivedStateOf {
            // Ensure progress is between 0.0 and 1.0
            val progressValue = 1f - (timeInMinutes * 60 - TimeUnit.MILLISECONDS.toSeconds(timeLeft)) / (timeInMinutes * 60f)
            maxOf(0f, minOf(1f, progressValue))
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress.value,
        animationSpec = tween(durationMillis = 1000)
    )

    Log.d("TimerProgressBar", "animatedProgress: $animatedProgress")
    Log.d("TimerProgressBar", "timeLeft: $timeLeft")

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
                color =Color(0xFFBC9FF1),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color =Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360 * (1f - animatedProgress),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            if (timeLeft < TimeUnit.MINUTES.toMillis(timeInMinutes.toLong())) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta =  ((360 * (1f - animatedProgress)) - 90f) * (PI / 180f).toFloat()
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
            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
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
                    text = taskTag.name.toLowerCase().replaceFirstChar { it.uppercase() },
                    color = Color(0xFFFF8D61),
                    fontSize = if (taskTag.name.length > 6) 18.sp else 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable {
                        showDialog = true
                    }
                )
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_edit_24),
                        contentDescription = "Edit TaskTag",
                        tint = Color(0xFFFF8D61)
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TimerCircularBarPreview() {

    val timerSharedViewModel = TimerSharedViewModel()
    TimerProgressBar(
        25,
        timerSharedViewModel = timerSharedViewModel
    ) {}

}