package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.yash.focusfusion.R
import com.yash.focusfusion.TimerService
import com.yash.focusfusion.core.util.Constants.CHECKINGSERVICESLOGS
import com.yash.focusfusion.core.util.Constants.CHECKINGSESSIONDATA
import com.yash.focusfusion.core.util.Constants.DATASTORELOGS
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetFocusTimeUseCase
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components.TimerProgressBar
import com.yash.focusfusion.ui.theme.SuccessGreen
import com.yash.focusfusion.ui.theme.fontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen(
    context: Context,
    timerSharedViewModel: TimerSharedViewModel,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel(),
) {

    val timer by timerSharedViewModel.initialFocusTime.collectAsState()
    val timeLeft by timerSharedViewModel.timeLeft.collectAsState()
    val extraTime by timerSharedViewModel.extraTime.collectAsState()
    val isTimerRunning by timerSharedViewModel.isRunning.collectAsState()
    val cancelTime by timerSharedViewModel.cancelTimeLeft.collectAsState()

    val firebaseAnalytics = Firebase.analytics

    var taskTag by remember {
        mutableStateOf(TaskTag.STUDY)
    }

    var isTakingExtraTime by remember {
        mutableStateOf(false)
    }

    val offset by animateDpAsState(
        targetValue = if (timeLeft == 0L) 0.dp else (-10).dp,
        animationSpec = tween(durationMillis = 500)
    )

    var scope = rememberCoroutineScope()

    var snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        
    ) {
        Text(
            text = if ((TimeUnit.MILLISECONDS.toSeconds(timeLeft) * 100) / 60 > 25)
                "You Can do it!" else "Just few minutes left",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 35.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily
        )

        TimerProgressBar(
            timerSharedViewModel = timerSharedViewModel,
            onTaskTagChanged = {
                taskTag = it
            }
        )

        if (extraTime > 0) {
            isTakingExtraTime = true

            val extraMinutes = extraTime / 60
            val extraSeconds = extraTime % 60
            Text(
                text = "+" + String.format("%02d:%02d", extraMinutes, extraSeconds),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 34.sp,
                fontFamily = FontFamily(listOf(Font(R.font.baloo_bold))),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .padding(10.dp)
                    .offset(y = offset)
                    .animateContentSize()
            )
        }

        if (!isTimerRunning) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(70.dp)
                    .shadow(
                        elevation = 5.dp,
                        shape = CircleShape,
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        timerSharedViewModel.updateIsRunning(true)


                        TimerService.startService(
                            context.applicationContext,
                            timer.toLong() * 60 * 1000,
                            taskTag.toString()
                        )

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {

            Button(
                onClick = {

                    val onStop = {
                        timerSharedViewModel.updateIsRunning(false)

                        // isTimerStarted = false // REMOVE THIS
                        TimerService.stopService(context.applicationContext)
                    }

                    if (TimeUnit.MILLISECONDS.toSeconds(cancelTime) > 0) {
                        onStop()
                    } else {

                        val endTime = System.currentTimeMillis()
                        val extraTimeInSeconds = if (extraTime > 0) extraTime else 0



                        scope.launch(Dispatchers.IO) {

                            viewModel.onEvent(
                                SessionEvent.InsertSession(
                                    Session(
                                        endTime,
                                        (TimeUnit.MINUTES.toSeconds(timer.toLong())
                                            .toInt() - TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                                            .toInt()) + extraTimeInSeconds.toInt(),
                                        taskTag
                                    )
                                )
                            )

                            val totalTimeInSeconds = (TimeUnit.MINUTES.toSeconds(timer.toLong())
                                .toInt() - TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                                .toInt()) + extraTimeInSeconds.toInt()

                            val bundle = Bundle()
                            bundle.putLong("end_time", endTime)
                            bundle.putInt(
                                "duration_inMinutes",
                                TimeUnit.SECONDS.toMinutes(totalTimeInSeconds.toLong()).toInt()
                            )
                            bundle.putString("taskTag", taskTag.toString())
                            firebaseAnalytics.logEvent("Session_Complete", bundle)

                        }

                        Log.d(
                            CHECKINGSESSIONDATA, "Time:- ${endTime}\n" +
                                "Duration:- ${
                                    (TimeUnit.MINUTES.toSeconds(timer.toLong())
                                        .toInt() - TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                                        .toInt()) + extraTimeInSeconds.toInt()
                                } Seconds\n" +
                                "Session Tag:- ${taskTag.name}"
                        )

                        Log.d(
                            CHECKINGSESSIONDATA,
                            "Session:- ${viewModel.sessionState.value.sessionEventType.name}"
                        )

                    }

                    onStop()
                },
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(
                    1.2.dp,
                    color = if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) > 0)
                        MaterialTheme.colorScheme.error else
                        SuccessGreen
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {

                if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) > 0) {
                    Text(
                        text = if (TimeUnit.MILLISECONDS.toSeconds(cancelTime) > 0) {
                            "Cancel ${TimeUnit.MILLISECONDS.toSeconds(cancelTime)}"
                        } else {
                            "Give Up!"
                        },
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Done!",
                        color = SuccessGreen,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(snackbarData = data)
            })
    }

}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerScreenPreview() {
    val context = LocalContext.current
    val timerSharedView = TimerSharedViewModel()
    TimerScreen(context = context, timerSharedViewModel = timerSharedView)
}