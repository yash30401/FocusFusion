package com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import javax.inject.Inject

class GetTotalSecondsForDateUseCase @Inject constructor(private val repository: SessionRepository) {

    suspend operator fun invoke(date:Long):Int{
        return repository.getTotalSecondsForDate(date)
    }
}