package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.core.util.Constants.CHECKINGVIEWMODEL
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.DatastoreUseCases
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionsUseCases: SessionUseCases,
    private val datastoreUseCases: DatastoreUseCases
) : ViewModel() {

    var sessionState = mutableStateOf(SessionState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.InsertSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        sessionsUseCases.insertSessionUseCase(event.session)
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.INSERTED,
                        )
                        Log.d(CHECKINGVIEWMODEL, "Entering InsertSession")
                        datastoreUseCases.calculateAndSaveStreakUseCase.invoke()
                    } catch (e: Exception) {
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.ERROR,
                            errorMessage = e.message
                        )
                        Log.d(CHECKINGVIEWMODEL, "Error:- ${e.message}")
                    }
                    val timeInMinutes = TimeUnit.SECONDS.toMinutes(
                        event.session.duration.toLong()
                    ).toInt()
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            if (timeInMinutes > 10) "Nice work! You crushed your ${timeInMinutes}-minutes focus session"
                            else "Oops! You stopped your focus session at the ${timeInMinutes}-minute mark."
                        )
                    )
                }
            }

            is SessionEvent.StopSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        sessionsUseCases.insertSessionUseCase(event.session)
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.INSERTED,
                        )
                    } catch (e: Exception) {
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.ERROR,
                            errorMessage = e.message
                        )
                    }
                    _eventFlow.emit(UIEvent.ShowSnackbar("Focus session interrupted. Record created"))
                }
            }

            is SessionEvent.UpdateSessionTag -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        sessionsUseCases.updateSessionUseCase(event.session)
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.UPDATED
                        )
                    } catch (e: Exception) {
                        sessionsUseCases.updateSessionUseCase(event.session)
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.ERROR,
                            errorMessage = e.message
                        )
                    }
                    _eventFlow.emit(UIEvent.ShowSnackbar("Your Focus Tag Updated"))
                }
            }


        }
    }


}

sealed class UIEvent() {
    data class ShowSnackbar(val message: String) : UIEvent()
}