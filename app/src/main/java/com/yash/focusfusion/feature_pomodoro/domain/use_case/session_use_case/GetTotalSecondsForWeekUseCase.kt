package com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import javax.inject.Inject

class GetTotalSecondsForWeekUseCase @Inject constructor(
    private val respository:SessionRepository
){
    suspend operator fun invoke(startTimestamp: Long,endTimestamp:Long):Int{
        return respository.getTotalSecondsForWeek(startTimestamp, endTimestamp)
    }
}