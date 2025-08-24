package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.focusfusion.core.util.generateDayBoxes
import com.yash.focusfusion.feature_pomodoro.domain.model.DayBox
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.DatastoreUseCases
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    private val datastoreUseCases: DatastoreUseCases,
) : ViewModel() {

    private val _lastWeekSessions = MutableStateFlow<List<Session>>(emptyList())
    val lastWeekSessions: StateFlow<List<Session>> get() = _lastWeekSessions

    private val _weeklySessions = MutableStateFlow<List<Session>>(emptyList())
    val weeklySessions: StateFlow<List<Session>> get() = _weeklySessions

    private val _currentDayHours = MutableStateFlow<Int>(0)
    val currentDayHours: StateFlow<Int> get() = _currentDayHours

    private val _streak = MutableStateFlow<Int>(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _heatMapDaysFlow = MutableStateFlow<Set<LocalDate>>(emptySet<LocalDate>())
    val heatMapDaysFlow: StateFlow<Set<LocalDate>> = _heatMapDaysFlow

    val heatMapWeeks: StateFlow<List<List<DayBox>>> = heatMapDaysFlow.map { dates ->
        withContext(Dispatchers.Default) { // Use Default dispatcher for CPU work
            if (dates.isNotEmpty()) {
                generateDayBoxes(dates)
            } else {
                emptyList()
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after UI is gone
        initialValue = emptyList() // Start with an empty list
    )

    val heatmapScroll: StateFlow<Int> = datastoreUseCases.getHeatmapScrollUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private var heatmapScrollJob: Job? = null

    init {
        getStreak()
        fetchAllSessionForHeatMapDates()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.WeekEvent -> fetchSessionsForWeek(
                event.startTimestamp,
                event.endTimestamp,
            )

            is HomeEvent.LastWeekEvent -> fetchSessionsForLastWeek(
                event.startTimestamp,
                event.endTimestamp,
            )

            is HomeEvent.todaysHours -> {
                getCurrentDayTotalHours(event.date)

            }

            is HomeEvent.HeatmapScrollEvent -> {
                heatmapScrollJob?.cancel()
                heatmapScrollJob = saveHeatmapScrollPosition(event.position)
            }
        }
    }

    private fun fetchSessionsForWeek(
        startTimestamp: Long,
        endTimestamp: Long,
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _weeklySessions.value = sessions
                    }

            } catch (e: Exception) {
                _weeklySessions.value = emptyList()
                Log.e(
                    "HomeScreenViewModel",
                    "Error fetching sessions for a week: $startTimestamp",
                    e
                )
            }
        }
    }

    private fun fetchAllSessionForHeatMapDates() {
        val dates = mutableSetOf<LocalDate>()

        viewModelScope.launch {
            try {
                sessionUseCases.getAllSessionsUseCase.invoke()
                    .map { sessions ->
                        // Transform Sessions to a Set of LocalDates
                        sessions.map {
                            Instant.ofEpochMilli(it.time)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }.toSet()
                    }
                    .collect { dates ->
                        _heatMapDaysFlow.update { dates }
                    }

            } catch (e: Exception) {
                Log.e(
                    "HomeScreenViewModel",
                    "Error fetching heatmap days",
                    e
                )
            }
        }


    }

    private fun saveHeatmapScrollPosition(position: Int) = viewModelScope.launch {
        try {
            datastoreUseCases.saveHeatmapScrollUseCase(position)
        } catch (e: Exception) {
            Log.e(
                "HomeScreenViewModel",
                "Error fetching heatmap scroll position",
                e
            )
        }
    }

    private fun fetchSessionsForLastWeek(
        startTimestamp: Long,
        endTimestamp: Long,
    ) {
        viewModelScope.launch {
            try {
                sessionUseCases.getSessionsForWeekUseCase(startTimestamp, endTimestamp)
                    .collect { sessions ->
                        _lastWeekSessions.value = sessions
                        Log.d("LASTWEEKRANGE", sessions.toString())
                    }
            } catch (e: Exception) {
                _lastWeekSessions.value = emptyList()
                Log.e(
                    "HomeScreenViewModel",
                    "Error fetching sessions for last week: $startTimestamp",
                    e
                )
            }
        }
    }

    private fun getCurrentDayTotalHours(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentDayHours.value = sessionUseCases.getTotalSecondsForDateUseCase(date)
            } catch (e: Exception) {
                _currentDayHours.value = -1
                Log.e("HomeScreenViewModel", "Error getting hours of the day", e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStreak() = viewModelScope.launch {
        datastoreUseCases.calculateAndSaveStreakUseCase()
        datastoreUseCases.getStreakCountUseCase().collect {
            _streak.value = it
            Log.d("STREAKWORK", "Viewmodel Streak Count:- $it")
        }
    }

    override fun onCleared() {
        super.onCleared()
        heatmapScrollJob?.cancel()
    }
}