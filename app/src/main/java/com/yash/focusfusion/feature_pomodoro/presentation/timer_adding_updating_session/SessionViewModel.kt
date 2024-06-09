package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.domain.use_case.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionsUseCases: SessionUseCases
):ViewModel() {

    var sessionState = mutableStateOf(SessionState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    fun onEvent(event:SessionEvent){
        when(event) {
            is SessionEvent.StopSession -> {
                viewModelScope.launch {
                    try{
                        sessionsUseCases.insertSessionUseCase(event.session)
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.INSERTED,
                        )
                    }catch (e:Exception){
                        sessionState.value = SessionState(
                            session = event.session,
                            sessionEventType = SessionEventType.ERROR,
                            errorMessage = e.message
                        )
                    }
                }
                _eventFlow.emit(UIEvent.StopSession)
            }
            is SessionEvent.UpdateSessionTag -> {

            }
        }
    }


}

sealed class UIEvent(){
    data class ShowSnackbar(val message:String):UIEvent()
    data class StopSession(val session: Session):UIEvent()
    data class  UpdateSessionTag(val session: Session,val taskTag: TaskTag):UIEvent()
}