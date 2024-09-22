package com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case

data class SessionUseCases(
    val insertSessionUseCase: InsertSessionUseCase,
    val updateSessionUseCase: UpdateSessionUseCase,
    val deleteSessionUseCase: DeleteSessionUseCase,
    val getSessionByIdUseCase: GetSessionByIdUseCase,
    val getAllSessionsUseCase: GetAllSessionsUseCase,
    val getTotalSecondsForDateUseCase: GetTotalSecondsForDateUseCase,
    val getTotalSecondsForWeekUseCase: GetTotalSecondsForWeekUseCase,
    val getTotalSecondsForMonthUseCase: GetTotalSecondsForMonthUseCase,
    val getTotalSecondsForYearUseCase: GetTotalSecondsForYearUseCase,
    val getSessionsForDateUseCase: GetSessionsForDateUseCase,
    val getSessionsForWeekUseCase: GetSessionsForWeekUseCase,
    val getSessionsForMonthUseCase: GetSessionsForMonthUseCase,
    val getSessionsForYearUseCase: GetSessionsForYearUseCase
)