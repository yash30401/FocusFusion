package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import com.yash.focusfusion.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    operator fun invoke():Flow<ThemeMode> = datastoreRepository.themeMode
}