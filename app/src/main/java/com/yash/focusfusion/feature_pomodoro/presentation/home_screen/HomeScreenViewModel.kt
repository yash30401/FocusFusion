package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.WeekEvent -> fetchSessionsForDay(
                event.startDate,
                event.endDate, event.month, event.year
            )
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

}