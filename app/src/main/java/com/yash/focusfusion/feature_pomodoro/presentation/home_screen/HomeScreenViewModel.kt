package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
) : ViewModel() {

    private val _todaySessions = MutableStateFlow<List<Session>>(emptyList())
    val todaySessions: StateFlow<List<Session>> get() = _todaySessions


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.DayEvent -> fetchSessionsForDay(event.date)
        }
    }


    private fun fetchSessionsForDay(date: Long) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForDateUseCase(date).collect { sessions ->
                    _todaySessions.value = sessions
                }
            } catch (e: Exception) {
                _todaySessions.value = emptyList()
                 Log.e("HomeScreenViewModel", "Error fetching sessions for date: $date", e)
            }
        }
    }

}