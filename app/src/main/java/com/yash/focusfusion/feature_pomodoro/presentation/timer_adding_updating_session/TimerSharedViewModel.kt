package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetFocusTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimerSharedViewModel() : ViewModel() {

    private val _initialFocusTime = MutableStateFlow(25)
    val initialFocusTime: StateFlow<Int> get() = _initialFocusTime.asStateFlow()

    private val _timeLeft = MutableStateFlow(1500000L)
    val timeLeft: StateFlow<Long> get() = _timeLeft

    private val _cancelTimeLeft = MutableStateFlow(10000L)
    val cancelTimeLeft: StateFlow<Long> get() = _cancelTimeLeft

    private val _extraTime = MutableStateFlow(0)
    val extraTime: StateFlow<Int> get() = _extraTime

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    private val _workState = MutableStateFlow(TaskTag.STUDY)
    val workState: StateFlow<TaskTag> = _workState.asStateFlow()


    fun updateFocusTime(value: Int) {
        _initialFocusTime.value = value
        if (!_isRunning.value) {
            _timeLeft.value = value * 60000L
        }
    }

    fun updateTimeLeft(time: Long) {
        _timeLeft.value = time
    }

    fun updateCancelTimeLeft(time: Long) {
        _cancelTimeLeft.value = time
        Log.d("CANCEL_TIME_VIEWMODEL", time.toString())
    }

    fun updateExtraTime(time: Int) {
        _extraTime.value = time
    }

    fun updateIsRunning(value: Boolean) {
        _isRunning.value = value
    }

    fun updateWorkTag(taskTag: TaskTag) {
        _workState.value = taskTag
    }

}