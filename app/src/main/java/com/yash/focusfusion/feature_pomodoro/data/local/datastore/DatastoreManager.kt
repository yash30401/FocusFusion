package com.yash.focusfusion.feature_pomodoro.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yash.focusfusion.core.util.Constants.DATASTORELOGS
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "timer_prefs")

class DatastoreManager(private val context: Context) {

    companion object {
        val TIME_LEFT_KEY = longPreferencesKey("TIME_LEFT")
        val EXTRA_TIME_KEY = intPreferencesKey("EXTRA_TIME")
        val CONTINUE_TIMER_KEY = booleanPreferencesKey("CONTINUE_TIMER")
        val CANCEL_TIME_LEFT_KEY = longPreferencesKey("CANCEL_TIME_LEFT")
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME_KEY")
        val ON_BOARDING_COMPLETED_KEY = booleanPreferencesKey("ON_BOARDING_COMPLETED_KEY")
        val TASK_TAG = stringPreferencesKey("TASK_TAG")
        val STREAK = intPreferencesKey("STREAK")
        val FOCUS_TIME_KEY = intPreferencesKey("FOCUS_TIME_KEY")
        val TIMER_START_TIME = longPreferencesKey("TIMER_START_TIME")
        val THEME_ORDINAL = intPreferencesKey("theme_ordinal")
        val IS_SESSION_END_SOUND_ENABLED = booleanPreferencesKey("IS_SESSION_END_SOUND_ENABLED")
        val HEATMAP_SCROLL = intPreferencesKey("HEATMAP_SCROLL")
    }

    val timeLeftFlow: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[TIME_LEFT_KEY] ?: 1500000L
        }

    val extraTime: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[EXTRA_TIME_KEY] ?: 0
    }

    val continueTimerFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[CONTINUE_TIMER_KEY] ?: false
        }

    val cancelTimeFlow: Flow<Long> = context.dataStore.data.map { prefrences ->
        prefrences[CANCEL_TIME_LEFT_KEY] ?: 10000L
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { prefrences ->
        prefrences[USER_NAME_KEY] ?: ""
    }

    val onBoardingCompletedFlow: Flow<Boolean> = context.dataStore.data.map { prefrences ->
        prefrences[ON_BOARDING_COMPLETED_KEY] ?: false
    }

    val taskTag: Flow<String> = context.dataStore.data.map { prefrences ->
        prefrences[TASK_TAG] ?: ""
    }

    val streak: Flow<Int> = context.dataStore.data.map { prefrences ->
        prefrences[STREAK] ?: 0
    }

    val focusTime: Flow<Int> = context.dataStore.data.map { prefrences ->
        prefrences[FOCUS_TIME_KEY] ?: 25
    }

    val timerStartTime: Flow<Long> = context.dataStore.data.map { prefrences ->
        prefrences[TIMER_START_TIME] ?: 0
    }

    val themeFlow = context.dataStore.data.map { preferences ->
        val ordinal = preferences[THEME_ORDINAL] ?: ThemeMode.SYSTEM.ordinal
        ThemeMode.values()[ordinal]
    }

    val isSessionEndSoundEnabled = context.dataStore.data.map { preferences ->
        preferences[IS_SESSION_END_SOUND_ENABLED] ?: true
    }

    val heatmapScroll: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[HEATMAP_SCROLL] ?: 0
    }

    suspend fun saveTimeLeft(timeLeft: Long) {
        try {
            context.dataStore.edit { preferences ->
                preferences[TIME_LEFT_KEY] = timeLeft
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving timeLeft", e)
        }
    }

    suspend fun saveExtraTime(extraTime: Int) {
        try {
            context.dataStore.edit { preferences ->
                preferences[EXTRA_TIME_KEY] = extraTime
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving extraTime", e)
        }
    }

    suspend fun saveContinueTimer(shouldContinue: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[CONTINUE_TIMER_KEY] = shouldContinue
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving continueTimer state", e)
        }
    }

    suspend fun saveCancelTimeLeft(cancelTime: Long) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[CANCEL_TIME_LEFT_KEY] = cancelTime
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving Cancel Time state", e)
        }
    }

    suspend fun saveUserName(name: String) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[USER_NAME_KEY] = name
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving user's name", e)
        }
    }

    suspend fun saveOnBoardingCompleted(isCompleted: Boolean) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[ON_BOARDING_COMPLETED_KEY] = isCompleted
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving user's name", e)
        }
    }

    suspend fun saveTaskTag(taskTag: String) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[TASK_TAG] = taskTag
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving task tag", e)
        }
    }

    suspend fun saveStreakCount(count: Int) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[STREAK] = count
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving streak count", e)
        }
    }

    suspend fun saveFocusTime(time: Int) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[FOCUS_TIME_KEY] = time
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving focus time.", e)
        }
    }

    suspend fun saveTimerStartTime(time: Long) {
        try {
            context.dataStore.edit { prefrences ->
                prefrences[TIMER_START_TIME] = time
            }
        } catch (e: Exception) {
            Log.e(DATASTORELOGS, "Error saving focus time.", e)
        }
    }

    suspend fun saveThemeMode(theme: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_ORDINAL] = theme.ordinal
        }
    }

    suspend fun saveIsSessionEndSoundEnabled(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SESSION_END_SOUND_ENABLED] = value
        }
    }

    suspend fun saveHeatmapScrol(position: Int) {
        context.dataStore.edit { preferences ->
            preferences[HEATMAP_SCROLL] = position
        }
    }
}

