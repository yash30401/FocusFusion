package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yash.focusfusion.core.util.Constants.CHECKINGSESSIONDATA
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components.TimerProgressBar
import com.yash.focusfusion.ui.theme.fontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timer: Int = 5,
    viewModel: SessionViewModel = hiltViewModel()
) {
    var isTimerRunning by remember { mutableStateOf(false) }
    var isTimerStarted by remember {
        mutableStateOf(false)
    }
    var timeLeft by remember { mutableStateOf(timer * 60) }

    var cancelTime by remember { mutableStateOf(10) }

    var startTime by remember {
        mutableStateOf(0L)
    }

    var taskTag by remember {
        mutableStateOf(TaskTag.STUDY)
    }

    var scope = rememberCoroutineScope()

    var scaffoldState = rememberBottomSheetScaffoldState()
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (cancelTime > 0) {
                delay(1000)
                cancelTime--
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collect{event->
            when(event){
                is UIEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.message
                )
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDFC)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = if ((timeLeft * 100) / 60 > 25) "You Can do it!" else "Just few minutes left",
            color = Color(0xFF212121),
            fontSize = 35.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily
        )

        TimerProgressBar(
            timeInMinutes = timer, isTimerRunning = isTimerRunning,
            isTimerStarted = isTimerStarted, timeLeft = timeLeft,
            onTaskTagChanged = {
                taskTag = it
            }
        ) { newTimeLeft ->
            timeLeft = newTimeLeft
        }

        if (isTimerRunning == false) {
            Column(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(
                        elevation = 5.dp,
                        shape = CircleShape,
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    )
                    .clip(CircleShape)
                    .background(Color(0xFFFF8D61))
                    .clickable {
                        isTimerRunning = true
                        isTimerStarted = true

                        startTime = System.currentTimeMillis()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
        } else {

            Button(
                onClick = {
                    if (cancelTime > 0) {
                        isTimerRunning = false
                        isTimerStarted = false
                        cancelTime = 10
                        timeLeft = timer * 60
                    } else {

                        val endTime = System.currentTimeMillis()
                        val duration = endTime - startTime
                        isTimerRunning = false
                        isTimerStarted = false
                        cancelTime = 10
                        timeLeft = timer * 60

                        scope.launch(Dispatchers.IO) {
                            viewModel.onEvent(SessionEvent.InsertSession(
                                Session(startTime,endTime,TimeUnit.MILLISECONDS.toMinutes(duration).toInt(),
                                    taskTag)
                            ))
                        }
                        Log.d(
                            CHECKINGSESSIONDATA, "Current Time:- ${startTime}\n" +
                                    "End Time:- ${endTime}\n" +
                                    "Duration:- ${TimeUnit.MILLISECONDS.toMinutes(duration).toInt()}\n" +
                                    "Session Tag:- ${taskTag.name}"
                        )

                        Log.d("CHECKINGSESSIONDATA" ,"Session:- ${viewModel.sessionState.value.errorMessage.toString()}")
                    }
                },
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(1.2.dp, color = Color(0xFFF45B5B)),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Text(
                    text = if (cancelTime > 0) {
                        "Cancel $cancelTime"
                    } else {
                        "Give Up!"
                    },
                    color = Color(0xFFF45B5B),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimerScreenPreview() {
    TimerScreen()
}