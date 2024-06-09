package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class GetSessionsForYearUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(year:String): Flow<List<Session>> {
        return repository.getSessionsForYear(year)
    }
}