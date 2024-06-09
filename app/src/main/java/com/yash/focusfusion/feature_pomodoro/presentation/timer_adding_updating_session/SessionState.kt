package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.yash.focusfusion.feature_pomodoro.domain.model.Session

data class SessionState(
    val session: Session?=null,
    val sessionEventType: SessionEventType = SessionEventType.INSERTED,
    val errorMessage: String?=null
)
