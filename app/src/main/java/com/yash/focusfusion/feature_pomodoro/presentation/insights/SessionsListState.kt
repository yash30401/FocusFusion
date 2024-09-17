package com.yash.focusfusion.feature_pomodoro.presentation.insights

import com.yash.focusfusion.feature_pomodoro.domain.model.Session

data class SessionsListState(
    val sessions: List<Session>? = null,
    val errorMessage: String? = null
)
