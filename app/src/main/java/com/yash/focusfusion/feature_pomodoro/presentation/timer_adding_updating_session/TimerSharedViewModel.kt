package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerSharedViewModel():ViewModel() {

    private val _timeLeft = MutableStateFlow(1500000L)
    val timeLeft:StateFlow<Long> get() = _timeLeft


    private val _cancelTimeLeft = MutableStateFlow(10000L)
    val cancelTimeLeft:StateFlow<Long> get() = _cancelTimeLeft

    private val _extraTime = MutableStateFlow(0)
    val extraTime:StateFlow<Int> get() = _extraTime

    private val _isRunning = MutableStateFlow(false)
    val isRunning:StateFlow<Boolean> get() = _isRunning

    fun updateTimeLeft(time:Long){
        _timeLeft.value = time
    }

    fun updateCancelTimeLeft(time: Long) {
        _cancelTimeLeft.value = time
        Log.d("CANCEL_TIME_VIEWMODEL",time.toString())
    }

    fun updateExtraTime(time: Int) {
        _extraTime.value = time
    }

    fun updateIsRunning(value: Boolean) {
        _isRunning.value = value
    }

}