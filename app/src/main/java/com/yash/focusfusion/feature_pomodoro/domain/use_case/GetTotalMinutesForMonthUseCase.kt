package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository

class GetTotalMinutesForMonthUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(month:String,year:String):Int{
        return repository.getTotalMinutesForMonth(month, year)
    }


}