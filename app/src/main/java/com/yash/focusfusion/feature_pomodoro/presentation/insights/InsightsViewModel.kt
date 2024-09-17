package com.yash.focusfusion.feature_pomodoro.presentation.insights

import androidx.compose.runtime.mutableStateOf
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.GetAllSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val sessionsUseCase: GetAllSessionsUseCase
) {
    var sessionsListState = mutableStateOf(SessionsListState())
        private set


}