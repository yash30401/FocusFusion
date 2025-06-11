package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import android.util.Log
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveUserNameUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
) {
    suspend operator fun invoke(name: String) = withContext(Dispatchers.IO) {
        try {
            if (name.isBlank()) return@withContext
            datastoreRepository.saveUserName(name)
        } catch (e: Exception) {
            Log.e("NAMEERROR", "Something going wrong while saving name")
            return@withContext
        }
    }
}