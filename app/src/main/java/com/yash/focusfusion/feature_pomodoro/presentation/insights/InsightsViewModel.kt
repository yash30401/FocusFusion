package com.yash.focusfusion.feature_pomodoro.presentation.insights

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.core.util.Constants.CHECKINGVIEWMODEL
import com.yash.focusfusion.core.util.Constants.INSIGHTSVIEWMODELCHECKING
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases
) : ViewModel() {

    private val _sessionListState = MutableStateFlow<List<Session>>(emptyList())
    val sessionListState: StateFlow<List<Session>> get() = _sessionListState

    var totalDurationState = mutableStateOf(0)
        private set

    fun onEvent(event: InsightsEvent) {
        when (event) {
            is InsightsEvent.DayEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessions = async {
                        try {
                            sessionUseCases.getSessionsForDateUseCase.invoke(event.date).collect {
                                _sessionListState.value = it
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for date:- ${sessionsListState.value.sessions}"
//                            )
                            }
                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            _sessionListState.value = emptyList()
                        }
                    }

                    val totalDuration = async {
                        try {
                            val totaldDuration =
                                sessionUseCases.getTotalSecondsForDateUseCase.invoke(event.date)
                            totalDurationState.value = totaldDuration
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for date:- ${sessionsListState.value.sessions}"
//                            )

                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            totalDurationState.value = 0
                        }
                    }
                    sessions.await()
                    totalDuration.await()
                }
            }

            is InsightsEvent.MonthEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessions = async {
                        try {
                            sessionUseCases.getSessionsForMonthUseCase.invoke(
                                event.month,
                                event.year
                            )
                                .collect {
                                    _sessionListState.value = it
//                                Log.d(
//                                    INSIGHTSVIEWMODELCHECKING,
//                                    "ALl session for month:- ${sessionListState.value}"
//                                )
                                }
                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            _sessionListState.value = emptyList()
                        }
                    }
                    val totalDuration = async {
                        try {
                            val totaldDuration =
                                sessionUseCases.getTotalSecondsForMonthUseCase.invoke(
                                    event.month,
                                    event.year
                                )
                            totalDurationState.value = totaldDuration
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for date:- ${sessionsListState.value.sessions}"
//                            )

                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            totalDurationState.value = 0
                        }
                    }
                    sessions.await()
                    totalDuration.await()
                }
            }

            is InsightsEvent.WeekEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessions = async {
                        try {
                            sessionUseCases.getSessionsForWeekUseCase.invoke(
                                event.startTimestamp,
                                event.endTimestamp,
                            ).collect {
                                _sessionListState.value = it
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for Week:- ${sessionListState.value}"
//                            )
                            }
                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            _sessionListState.value = emptyList()
                        }
                    }
                    val totalDuration = async {
                        try {
                            val totaldDuration =
                                sessionUseCases.getTotalSecondsForWeekUseCase.invoke(
                                    event.startTimestamp,
                                    event.endTimestamp,
                                )
                            totalDurationState.value = totaldDuration
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for date:- ${sessionsListState.value.sessions}"
//                            )

                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            totalDurationState.value = 0
                        }
                    }
                    sessions.await()
                    totalDuration.await()
                }
            }

            is InsightsEvent.YearEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessions = async {
                        try {
                            sessionUseCases.getSessionsForYearUseCase.invoke(
                                event.year
                            ).collect {
                                _sessionListState.value = it
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for Year:- ${_sessionListState.value}"
//                            )
                            }
                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            _sessionListState.value = emptyList()
                        }
                    }
                    val totalDuration = async {
                        try {
                            val totaldDuration =
                                sessionUseCases.getTotalSecondsForYearUseCase.invoke(
                                    event.year
                                )
                            totalDurationState.value = totaldDuration
//                            Log.d(
//                                INSIGHTSVIEWMODELCHECKING,
//                                "ALl session for date:- ${sessionsListState.value.sessions}"
//                            )

                        } catch (e: Exception) {
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Error:- ${e.message}")
                            totalDurationState.value = 0
                        }
                    }
                    sessions.await()
                    totalDuration.await()
                }
            }
        }
    }

}