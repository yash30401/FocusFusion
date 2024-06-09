package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag

sealed class SessionEvent(){
    data class InsertSession(val session: Session):SessionEvent()
    data class StopSession(val session: Session):SessionEvent()

    data class UpdateSessionTag(val session: Session,val taskTag: TaskTag):SessionEvent()
}