package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import com.yash.focusfusion.ui.theme.ThemeMode
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
) {
    suspend operator fun invoke(theme: ThemeMode) {
        datastoreRepository.saveThemeMode(theme)
    }
}