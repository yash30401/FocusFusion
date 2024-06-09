package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository

class GetTotalMinutesForYearUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(year:String):Int{
        return repository.getTotalMinutesForYear(year)
    }
}