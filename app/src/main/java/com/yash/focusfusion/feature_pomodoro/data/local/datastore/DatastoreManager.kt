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
import androidx.datastore.preferences.preferencesDataStore
import com.yash.focusfusion.core.util.Constants.DATASTORELOGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "timer_prefs")

class DatastoreManager(private val context: Context) {

    companion object {
        val TIME_LEFT_KEY = longPreferencesKey("TIME_LEFT")
        val EXTRA_TIME_KEY = intPreferencesKey("EXTRA_TIME")
        val CONTINUE_TIMER_KEY = booleanPreferencesKey("CONTINUE_TIMER")
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
}

