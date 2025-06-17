package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.DatastoreUseCases
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    private val datastoreUseCases: DatastoreUseCases,
) : ViewModel() {

    private val _lastWeekSessions = MutableStateFlow<List<Session>>(emptyList())
    val lastWeekSessions: StateFlow<List<Session>> get() = _lastWeekSessions

    private val _weeklySessions = MutableStateFlow<List<Session>>(emptyList())
    val weeklySessions: StateFlow<List<Session>> get() = _weeklySessions

    private val _currentDayHours = MutableStateFlow<Int>(0)
    val currentDayHours: StateFlow<Int> get() = _currentDayHours

    private val _streak = MutableStateFlow<Int>(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    init {
        getStreak()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        endTimestamp: Long,
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _weeklySessions.value = sessions
                    }

            } catch (e: Exception) {
                _weeklySessions.value = emptyList()
                Log.e(
                    "HomeScreenViewModel",
                    "Error fetching sessions for a week: $startTimestamp",
                    e
                )
            }
        }
    }

    private fun fetchSessionsForLastWeek(
        startTimestamp: Long,
        endTimestamp: Long,
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _lastWeekSessions.value = sessions
                        Log.d("LASTWEEKRANGE", sessions.toString())
                    }
            } catch (e: Exception) {
                _lastWeekSessions.value = emptyList()
                Log.e(
                    "HomeScreenViewModel",
                    "Error fetching sessions for last week: $startTimestamp",
                    e
                )
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStreak() = viewModelScope.launch {
        datastoreUseCases.getStreakCountUseCase().collect {
            _streak.value = it
            Log.d("STREAKWORK", "Viewmodel Streak Count:- $it")
        }
    }
}