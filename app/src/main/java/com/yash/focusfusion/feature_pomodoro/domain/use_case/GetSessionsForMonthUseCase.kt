package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSessionsForMonthUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(month:String,year:String): Flow<List<Session>> {
        if(month.isBlank() or year.isBlank()){
            return flow {  }
        }
        return repository.getSessionsForMonth(month,year)
    }
}