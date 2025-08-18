package com.yash.focusfusion.feature_pomodoro.domain.repository

import com.yash.focusfusion.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {

    val timeLeftFlow: Flow<Long>
    val extraTimeFlow: Flow<Int>
    val continueTimerFlow: Flow<Boolean>
    val cancelTimeFlow: Flow<Long>
    val userNameFlow: Flow<String>
    val isOnBoardingCompletedFlow: Flow<Boolean>
    val streak: Flow<Int>
    val focusTime: Flow<Int>
    val themeMode: Flow<ThemeMode>

    suspend fun saveTimeLeft(time: Long)
    suspend fun saveExtraTime(extraTime: Int)
    suspend fun saveContinueTimer(shouldContinue: Boolean)
    suspend fun saveCancelTimeLeft(cancelTime: Long)
    suspend fun saveUserName(name: String)
    suspend fun onBoardingCompleted(isCompleted: Boolean)
    suspend fun saveStreakCount(count: Int)
    suspend fun saveFocusTime(time: Int)
    suspend fun saveThemeMode(theme: ThemeMode)
}