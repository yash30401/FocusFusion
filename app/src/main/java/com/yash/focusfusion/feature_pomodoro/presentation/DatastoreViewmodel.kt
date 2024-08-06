package com.yash.focusfusion.feature_pomodoro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.DatastoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatastoreViewmodel @Inject constructor(
    private val datastoreUseCases: DatastoreUseCases
) : ViewModel() {

    private val _timeLeftFlow: StateFlow<Long> = datastoreUseCases.getTimeLeftUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0L
    )

    private val _extraTimeFlow: StateFlow<Int> = datastoreUseCases.getExtraTimeUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private val _continueTimerFlow: StateFlow<Boolean> =
        datastoreUseCases.getContinueTimerUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private val _cancelTimeLeftFlow: StateFlow<Long> =
        datastoreUseCases.getCancelTimeUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0L
        )

    val timeLeftFlow: StateFlow<Long> = _timeLeftFlow
    val extraTimeFlow: StateFlow<Int> = _extraTimeFlow
    val continueTimerFlow: StateFlow<Boolean> = _continueTimerFlow
    val cancelTimeLeftFlow: StateFlow<Long> = _cancelTimeLeftFlow

    suspend fun saveTimeLeft(timeLeft: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreUseCases.saveTimeLeftUseCase(timeLeft)
        }
    }

    suspend fun saveExtraTime(extraTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreUseCases.saveExtraTimeUseCase(extraTime)
        }
    }

    suspend fun saveContinueTimer(shouldContinue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreUseCases.saveContinueTimerUseCase(shouldContinue)
        }
    }

    suspend fun saveCancelTimeLeft(cancelTimeLeft: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreUseCases.saveCancelTimeLeftUseCase(cancelTimeLeft)
        }
    }
}