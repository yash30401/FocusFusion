package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository

class GetTotalMinutesForYearUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(year:String):Int{
        if(year.isBlank()){
            return -1
        }
        return repository.getTotalMinutesForYear(year)
    }
}