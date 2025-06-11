package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import android.util.Log
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
) {
    suspend operator fun invoke(): String = withContext(Dispatchers.IO) {
        try {
            return@withContext datastoreRepository.userNameFlow.first()
        }catch (e: Exception){
            Log.e("NAMEERROR", "Something going wrong while fetching users name")
            return@withContext ""
        }
    }
}