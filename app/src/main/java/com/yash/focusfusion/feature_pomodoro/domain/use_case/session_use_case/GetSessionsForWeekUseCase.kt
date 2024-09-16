package com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionsForWeekUseCase @Inject constructor(private val respository: SessionRepository) {

    operator fun invoke(
        startWeek: String,
        endWeek: String,
        month: String,
        year: String
    ): Flow<List<Session>> {
        return respository.getSessionsForWeek(startWeek, endWeek, month, year)
    }
}