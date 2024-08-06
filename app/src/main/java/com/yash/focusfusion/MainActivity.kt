package com.yash.focusfusion

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.presentation.DatastoreViewmodel
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerScreen
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerSharedViewModel
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private var timeLeft: Long by mutableStateOf(1500000L) // Default to 25:00
    private var cancelTimeLeft:Long by mutableStateOf(10000L)

    private val datastoreViewmodel: DatastoreViewmodel by viewModels()

    private var extraTime: Int by mutableStateOf(0)
    private var isTimerRunning: Boolean by mutableStateOf(false)
    private val timerSharedViewModel:TimerSharedViewModel by viewModels()

    private val timerUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            timeLeft = intent?.getLongExtra("TIME_LEFT", 0L) ?: 0L
            cancelTimeLeft = intent?.getLongExtra("CANCEL_TIME_LEFT", 0L) ?: 0L
            Log.d("CANCEL_TIME_MAIN_ACITIVITY",cancelTimeLeft.toString())
            extraTime = intent?.getIntExtra("EXTRA_TIME", 0) ?: 0
            isTimerRunning = timeLeft>0

            lifecycleScope.launch  (Dispatchers.IO){
                datastoreViewmodel.saveTimeLeft(timeLeft)
                datastoreViewmodel.saveExtraTime(extraTime)
                datastoreViewmodel.saveContinueTimer(isTimerRunning)

                timerSharedViewModel.updateTimeLeft(timeLeft)
                timerSharedViewModel.updateExtraTime(extraTime)
                timerSharedViewModel.updateIsRunning(isTimerRunning)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                datastoreViewmodel.saveCancelTimeLeft(cancelTimeLeft)
                timerSharedViewModel.updateCancelTimeLeft(cancelTimeLeft)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        registerReceiver(
            timerUpdateReceiver, IntentFilter("TIMER_UPDATE"),
            RECEIVER_EXPORTED
        )

        lifecycleScope.launch  (Dispatchers.IO){
            datastoreViewmodel.timeLeftFlow.collect { savedTimeLeft ->
                timeLeft = savedTimeLeft
                timerSharedViewModel.updateTimeLeft(savedTimeLeft)
            }
        }

        lifecycleScope.launch (Dispatchers.IO){
            datastoreViewmodel.cancelTimeLeftFlow.collect{savedCancelTimeLeft->
                cancelTimeLeft = savedCancelTimeLeft
                timerSharedViewModel.updateCancelTimeLeft(savedCancelTimeLeft)
            }
        }

        lifecycleScope.launch (Dispatchers.IO) {
            datastoreViewmodel.extraTimeFlow.collect { savedExtraTime ->
                extraTime = savedExtraTime
                timerSharedViewModel.updateExtraTime(savedExtraTime)
            }
        }

        lifecycleScope.launch (Dispatchers.IO) {

            datastoreViewmodel.continueTimerFlow.collect { shouldContinue ->
                isTimerRunning = shouldContinue
                timerSharedViewModel.updateIsRunning(shouldContinue)
            }
        }

        setContent {
            FocusFusionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TimerScreen(context = this@MainActivity, timerSharedViewModel = timerSharedViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerUpdateReceiver)
    }
}
