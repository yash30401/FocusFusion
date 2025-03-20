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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
) : ViewModel() {

    private val _weeklySessions = MutableStateFlow<List<Session>>(emptyList())
    val weeklySessions: StateFlow<List<Session>> get() = _weeklySessions

    private val _currentDayHours = MutableStateFlow<Int>(0)
    val currentDayHours: StateFlow<Int> get() = _currentDayHours

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.WeekEvent -> fetchSessionsForDay(
                event.startDate,
                event.endDate, event.month, event.year
            )

            is HomeEvent.todaysHours -> {
                getCurrentDayTotalHours(event.date)
            }
        }
    }

    private fun fetchSessionsForDay(
        startDate: String,
        endDate: String,
        month: String,
        year: String,
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startDate, endDate, month, year)
                    .collect { sessions ->
                        _weeklySessions.value = sessions
                    }

            } catch (e: Exception) {
                _weeklySessions.value = emptyList()
                Log.e("HomeScreenViewModel", "Error fetching sessions for a week: $startDate", e)
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