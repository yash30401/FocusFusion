package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTotalMinutesForMonthUseCase @Inject constructor(private val repository: SessionRepository) {

    suspend operator fun invoke(month:String,year:String):Int{
        if(month.isBlank() or year.isBlank()){
            return -1
        }
        return repository.getTotalMinutesForMonth(month, year)
    }


}