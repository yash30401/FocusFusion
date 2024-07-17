package com.yash.focusfusion.feature_pomodoro.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yash.focusfusion.core.util.Constants.DATASTORELOGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastoreManager(private val context: Context) {


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TimerPreferences")

        val EXITED_TIME_KEY = longPreferencesKey("EXITED_TIME_KEY")
        val TIME_LEFT_KEY = intPreferencesKey("TIME_LEFT_KEY")
        const val DEFAULT_LONG_VALUE = -1L
        const val DEFAULT_INT_VALUE = -1
    }

    suspend fun addData(exitedTime: Long?, timeLeft: Int?) {
        Log.d(DATASTORELOGS,"Entering Add Data")
        context.dataStore.edit { timerPreferences ->
            timerPreferences[EXITED_TIME_KEY] = exitedTime ?: DEFAULT_LONG_VALUE
            timerPreferences[TIME_LEFT_KEY] = timeLeft ?: DEFAULT_INT_VALUE
            Log.d(DATASTORELOGS, "Saved exitedTime: $exitedTime, timeLeft: $timeLeft")
        }
    }

    val exitedTimeData: Flow<Long?> = context.dataStore.data.map { preferences ->
        val exitedTime = preferences[EXITED_TIME_KEY] ?: DEFAULT_LONG_VALUE
        if (exitedTime == DEFAULT_LONG_VALUE) null else exitedTime
    }

    val timeLeftData: Flow<Int?> = context.dataStore.data.map { preferences ->
        val timeLeft = preferences[TIME_LEFT_KEY] ?: DEFAULT_INT_VALUE
        if (timeLeft == DEFAULT_INT_VALUE) null else timeLeft
    }
}
