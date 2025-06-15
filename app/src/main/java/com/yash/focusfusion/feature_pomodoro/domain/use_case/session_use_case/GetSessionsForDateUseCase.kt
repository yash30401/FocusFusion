package com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case

import android.util.Log
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionsForDateUseCase @Inject constructor(private val repository: SessionRepository) {

    operator fun invoke(date:Long): Flow<List<Session>> {
        return repository.getSessionsForDate(date)
    }
}