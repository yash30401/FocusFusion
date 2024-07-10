package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.core.util.Constants.CHECKINGVIEWMODEL
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.domain.use_case.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionsUseCases: SessionUseCases
) : ViewModel() {

    var sessionState = mutableStateOf(SessionState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
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
                        Log.d(CHECKINGVIEWMODEL,"Entering InsertSession")
                    } catch (e: Exception) {
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.ERROR,
                            errorMessage = e.message
                        )
                        Log.d(CHECKINGVIEWMODEL,"Error:- ${e.message}")
                    }
                    _eventFlow.emit(UIEvent.ShowSnackbar("Nice work! You crushed your ${event.session.duration}-minute focus session"))
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
                    }catch (e:Exception){
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