package com.yash.focusfusion.feature_pomodoro.domain.use_case

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import javax.inject.Inject

class GetSessionByIdUseCase @Inject constructor(private val repository: SessionRepository) {

    suspend operator fun invoke(id:Int):Session?{
        return repository.getSessionById(id)
    }
}