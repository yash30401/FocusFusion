package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import android.util.Log
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStreakCountUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
) {

    operator fun invoke(): Flow<Int>  {
        Log.d("STREAKWORK"," UseCase:- ${datastoreRepository.streak.toString()}")
        return datastoreRepository.streak
    }

}