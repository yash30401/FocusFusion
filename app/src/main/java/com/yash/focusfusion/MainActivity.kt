package com.yash.focusfusion

import TimerService
import TimerService.Companion.ACTION_START
import TimerService.Companion.EXTRA_TIME_LEFT
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.yash.focusfusion.core.util.Constants.TIMERRUNNINGLOGS
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.SessionViewModel
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerScreen
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var timerReceiver: BroadcastReceiver
    private var isTimerRunning:Boolean = false


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val timeLeft = intent?.getIntExtra("timeLeft", 0) ?: 0
                Log.d("TIMER_UPDATE", "Time left: $timeLeft")
            }
        }
        val filter = IntentFilter("TIMER_UPDATE")
        registerReceiver(timerReceiver, filter, RECEIVER_NOT_EXPORTED)
        setContent {
            FocusFusionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TimerScreen(){isTimerOn->
                        isTimerRunning = isTimerOn
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerReceiver)
        if(isTimerRunning){
            Log.d(TIMERRUNNINGLOGS,"App Closed At:- ${System.currentTimeMillis()}")
        }
    }
}
