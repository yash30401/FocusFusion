package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
) : ViewModel() {

    private val _lastWeekSessions = MutableStateFlow<List<Session>>(emptyList())
    val lastWeekSessions: StateFlow<List<Session>> get() = _lastWeekSessions

    private val _weeklySessions = MutableStateFlow<List<Session>>(emptyList())
    val weeklySessions: StateFlow<List<Session>> get() = _weeklySessions

    private val _currentDayHours = MutableStateFlow<Int>(0)
    val currentDayHours: StateFlow<Int> get() = _currentDayHours

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.WeekEvent -> fetchSessionsForWeek(
                event.startTimestamp,
                event.endTimestamp,
            )

            is HomeEvent.LastWeekEvent -> fetchSessionsForLastWeek(
                event.startTimestamp,
                event.endTimestamp,
            )

            is HomeEvent.todaysHours -> {
                getCurrentDayTotalHours(event.date)
            }
        }
    }

    private fun fetchSessionsForWeek(
       startTimestamp: Long,
        endTimestamp: Long
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _weeklySessions.value = sessions
                    }

            } catch (e: Exception) {
                _weeklySessions.value = emptyList()
                Log.e("HomeScreenViewModel", "Error fetching sessions for a week: $startTimestamp", e)
            }
        }
    }

    private fun fetchSessionsForLastWeek(
        startTimestamp:Long,
        endTimestamp:Long
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _lastWeekSessions.value = sessions
                        Log.d("LASTWEEKRANGE",sessions.toString())
                    }
            } catch (e: Exception) {
                _lastWeekSessions.value = emptyList()
                Log.e("HomeScreenViewModel", "Error fetching sessions for last week: $startTimestamp", e)
            }
        }
    }

    private fun getCurrentDayTotalHours(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentDayHours.value = sessionUseCases.getTotalSecondsForDateUseCase(date)
            } catch (e: Exception) {
                _currentDayHours.value = -1
                Log.e("HomeScreenViewModel", "Error getting hours of the day", e)
            }
        }
    }


}