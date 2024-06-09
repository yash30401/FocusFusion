package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSessionsForYearUseCase @Inject constructor(private val repository: SessionRepository) {

    suspend operator fun invoke(year:String): Flow<List<Session>> {
        if(year.isBlank()){
            return flow {  }
        }
        return repository.getSessionsForYear(year)
    }
}