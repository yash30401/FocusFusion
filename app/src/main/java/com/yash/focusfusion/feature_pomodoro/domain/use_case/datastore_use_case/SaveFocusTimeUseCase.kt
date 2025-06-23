package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveFocusTimeUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {

    suspend operator fun invoke(time:Int) = withContext(Dispatchers.IO) {
        datastoreRepository.saveFocusTime(time)
    }
}