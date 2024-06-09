package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository

class InsertSessionUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke(session: Session){
        repository.insertSession(session)
    }

}