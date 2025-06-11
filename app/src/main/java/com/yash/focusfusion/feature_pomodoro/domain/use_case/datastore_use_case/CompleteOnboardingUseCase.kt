package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import android.util.Log
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        try {
            return@withContext datastoreRepository.onBoardingCompleted(true)
        }catch (e: Exception){
            Log.e("NAMEERROR", "Something going wrong while doing onboarding")
            return@withContext
        }
    }
}