package com.yash.focusfusion.feature_pomodoro.presentation.insights

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.core.util.Constants.CHECKINGVIEWMODEL
import com.yash.focusfusion.core.util.Constants.INSIGHTSVIEWMODELCHECKING
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases
) : ViewModel() {
    var sessionsListState = mutableStateOf(SessionsListState())
        private set


    fun onEvent(event: InsightsEvent) {
        when (event) {
            is InsightsEvent.DayEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        sessionUseCases.getSessionsForDateUseCase.invoke(event.date).collect {
                            sessionsListState.value = SessionsListState(
                                sessions = it
                            )
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for date:- ${sessionsListState.value.sessions}"
                            )
                        }
                    } catch (e: Exception) {
                        Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                        sessionsListState.value = SessionsListState(
                            sessions = emptyList(),
                            errorMessage = e.message
                        )
                    }
                }
            }

            is InsightsEvent.MonthEvent -> {}
            is InsightsEvent.WeekEvent -> {}
            is InsightsEvent.YearEvent -> {}
        }
    }

}