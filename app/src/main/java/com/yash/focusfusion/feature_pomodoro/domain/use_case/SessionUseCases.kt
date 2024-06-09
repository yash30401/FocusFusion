package com.yash.focusfusion.feature_pomodoro.domain.use_case

data class SessionUseCases(
    val insertSessionUseCase: InsertSessionUseCase,
    val updateSessionUseCase: UpdateSessionUseCase,
    val deleteSessionUseCase: DeleteSessionUseCase,
    val getSessionByIdUseCase: GetSessionByIdUseCase,
    val getAllSessionsUseCase: GetAllSessionsUseCase,
    val getTotalMinutesForDateUseCase: GetTotalMinutesForDateUseCase,
    val getTotalMinutesForMonthUseCase: GetTotalMinutesForMonthUseCase,
    val getTotalMinutesForYearUseCase: GetTotalMinutesForYearUseCase,
    val getSessionForDateUseCase: GetSessionsForDateUseCase,
    val getSessionsForMonthUseCase: GetSessionsForMonthUseCase,
    val getSessionsForYearUseCase: GetSessionsForYearUseCase
)