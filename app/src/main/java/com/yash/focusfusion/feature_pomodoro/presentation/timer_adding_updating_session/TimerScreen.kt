package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components.TimerProgressBar
import com.yash.focusfusion.ui.theme.fontFamily

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    ) {
    var isTimerRunning by remember { mutableStateOf(false) }
    var isTimerStarted by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "You Can do it!",
            color = Color(0xFF212121),
            fontSize = 35.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily
        )

        TimerProgressBar(timeInMinutes = 1, isTimerRunning, isTimerStarted) {
            isTimerRunning = false
        }

        if(isTimerRunning == true) {
            Column(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF8D61))
                    .clickable {
                        isTimerRunning = !isTimerRunning
                        isTimerStarted = true
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
        }else{
            Column(modifier = Modifier.border(width = 1.dp,
                color = Color(0xFF))) {
                Text(text = "Give Up!")
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