package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class GetAllSessionsUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(): Flow<List<Session>> {
        return repository.getAllSessions()
    }
}