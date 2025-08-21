package com.yash.focusfusion.feature_pomodoro.data.repository

import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import com.yash.focusfusion.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

class DatastoreRepositoryImpl(private val datastoreManager: DatastoreManager) :
    DatastoreRepository {
    override val timeLeftFlow: Flow<Long>
        get() = datastoreManager.timeLeftFlow

    override val extraTimeFlow: Flow<Int>
        get() = datastoreManager.extraTime

    override val continueTimerFlow: Flow<Boolean>
        get() = datastoreManager.continueTimerFlow

    override val cancelTimeFlow: Flow<Long>
        get() = datastoreManager.cancelTimeFlow

    override val userNameFlow: Flow<String>
        get() = datastoreManager.userNameFlow

    override val isOnBoardingCompletedFlow: Flow<Boolean>
        get() = datastoreManager.onBoardingCompletedFlow

    override val streak: Flow<Int>
        get() = datastoreManager.streak

    override val focusTime: Flow<Int>
        get() = datastoreManager.focusTime

    override val themeMode: Flow<ThemeMode>
        get() = datastoreManager.themeFlow

    override val isSessionEndSoundEnabled: Flow<Boolean>
        get() = datastoreManager.isSessionEndSoundEnabled

    override suspend fun saveTimeLeft(time: Long) {
        datastoreManager.saveTimeLeft(time)
    }

    override suspend fun saveExtraTime(extraTime: Int) {
        datastoreManager.saveExtraTime(extraTime)
    }

    override suspend fun saveContinueTimer(shouldContinue: Boolean) {
        datastoreManager.saveContinueTimer(shouldContinue)
    }

    override suspend fun saveCancelTimeLeft(cancelTime: Long) {
        datastoreManager.saveCancelTimeLeft(cancelTime)
    }

    override suspend fun saveUserName(name: String) {
        datastoreManager.saveUserName(name)
    }

    override suspend fun onBoardingCompleted(isCompleted: Boolean) {
        datastoreManager.saveOnBoardingCompleted(isCompleted)
    }

    override suspend fun saveStreakCount(count: Int) {
        datastoreManager.saveStreakCount(count)
    }

    override suspend fun saveFocusTime(time: Int) {
        datastoreManager.saveFocusTime(time)
    }

    override suspend fun saveThemeMode(theme: ThemeMode) {
        datastoreManager.saveThemeMode(theme)
    }

    override suspend fun saveIsSessionEndSoundEnabled(value: Boolean) {
        datastoreManager.saveIsSessionEndSoundEnabled(value)
    }
}