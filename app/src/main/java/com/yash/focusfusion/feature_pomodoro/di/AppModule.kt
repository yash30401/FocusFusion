package com.yash.focusfusion.feature_pomodoro.di

import android.app.Application
import androidx.room.Room
import com.yash.focusfusion.core.util.Constants.DATABASE_NAME
import com.yash.focusfusion.feature_pomodoro.data.local.SessionDatabase
import com.yash.focusfusion.feature_pomodoro.data.repository.SessionRepositoryImpl
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import com.yash.focusfusion.feature_pomodoro.domain.use_case.DeleteSessionUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetAllSessionsUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetSessionByIdUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetSessionsForDateUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetSessionsForMonthUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetSessionsForYearUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetTotalMinutesForDateUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetTotalMinutesForMonthUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.GetTotalMinutesForYearUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.InsertSessionUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.SessionUseCases
import com.yash.focusfusion.feature_pomodoro.domain.use_case.UpdateSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn
object AppModule {

    @Provides
    @Singleton
    fun provideSessionDatabase(application: Application): SessionDatabase {
        return Room.databaseBuilder(
            application,
            SessionDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSessionRepository(database: SessionDatabase): SessionRepositoryImpl {
        return SessionRepositoryImpl(database.dao)
    }

    @Provides
    @Singleton
    fun provideSessionUseCases(sessionRepository: SessionRepository): SessionUseCases {
        return SessionUseCases(
            insertSessionUseCase = InsertSessionUseCase(sessionRepository),
            updateSessionUseCase = UpdateSessionUseCase(sessionRepository),
            deleteSessionUseCase = DeleteSessionUseCase(sessionRepository),
            getSessionByIdUseCase = GetSessionByIdUseCase(sessionRepository),
            getAllSessionsUseCase = GetAllSessionsUseCase(sessionRepository),
            getTotalMinutesForDateUseCase = GetTotalMinutesForDateUseCase(sessionRepository),
            getTotalMinutesForMonthUseCase = GetTotalMinutesForMonthUseCase(sessionRepository),
            getTotalMinutesForYearUseCase = GetTotalMinutesForYearUseCase(sessionRepository),
            getSessionForDateUseCase = GetSessionsForDateUseCase(sessionRepository),
            getSessionsForMonthUseCase = GetSessionsForMonthUseCase(sessionRepository),
            getSessionsForYearUseCase = GetSessionsForYearUseCase(sessionRepository)
        )
    }
}