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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yash.focusfusion.core.util.Constants.DATASTORELOGS
import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.SessionViewModel
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerScreen
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var timerReceiver: BroadcastReceiver
    private var isTimerRunning: Boolean = false
    private var timeLeft: Int? = null
    private lateinit var datastoreManager: DatastoreManager
    private lateinit var exitedTimeData:MutableStateFlow<Long?>



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

        exitedTimeData = MutableStateFlow<Long?>(null)

        datastoreManager = DatastoreManager(this)
        lifecycleScope.launch {
            datastoreManager.exitedTimeData.collect {
                data.value = it
                if (data.value != null) {
                    Log.d(DATASTORELOGS, "Time We are getting is:- ${data.value}")
                } else {
                    Log.d(DATASTORELOGS, "Getting Null")
                }
            }
        }


        setContent {
            FocusFusionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TimerScreen() { isTimerOn, newTimeLeft ->
                        isTimerRunning = isTimerOn
                        timeLeft = newTimeLeft
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerReceiver)
        runBlocking {
            if (isTimerRunning) {
                Log.d(DATASTORELOGS, "App Closed At: ${System.currentTimeMillis()}")
                datastoreManager.addData(System.currentTimeMillis(), timeLeft)
            } else {
                datastoreManager.addData(null, null)
            }
        }
    }
}
