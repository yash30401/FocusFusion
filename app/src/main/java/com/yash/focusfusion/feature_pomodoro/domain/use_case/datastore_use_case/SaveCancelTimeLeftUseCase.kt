package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import javax.inject.Inject

class SaveCancelTimeLeftUseCase @Inject constructor(private val datastoreRepository: DatastoreRepository) {

    suspend operator fun invoke(cancelTimeLeft: Long) {
        datastoreRepository.saveCancelTimeLeft(cancelTimeLeft)
    }
}