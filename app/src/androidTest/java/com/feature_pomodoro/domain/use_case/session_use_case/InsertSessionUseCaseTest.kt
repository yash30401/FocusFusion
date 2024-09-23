package com.feature_pomodoro.domain.use_case.session_use_case

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yash.focusfusion.feature_pomodoro.data.local.SessionDao
import com.yash.focusfusion.feature_pomodoro.data.local.SessionDatabase
import com.feature_pomodoro.data.repository.FakeSessionRepository
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.GetAllSessionsUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.InsertSessionUseCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class InsertSessionUseCaseTest {

    private lateinit var sessionDatabase: SessionDatabase
    private lateinit var sessionDao: SessionDao
    private lateinit var insertSession: InsertSessionUseCase
    private lateinit var getAllSessionsUseCase: GetAllSessionsUseCase

    private lateinit var fakeSessionRepository: FakeSessionRepository

    @Before
    fun setUp() {
        sessionDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SessionDatabase::class.java
        ).allowMainThreadQueries().build()
        sessionDao = sessionDatabase.dao

        fakeSessionRepository = FakeSessionRepository(sessionDao)
        insertSession = InsertSessionUseCase(fakeSessionRepository)
        getAllSessionsUseCase = GetAllSessionsUseCase(fakeSessionRepository)


    }

    @After
    fun tearDown() {
        sessionDatabase.close()
    }

    @Test
    fun InsertNote() = runBlocking {
        val session = Session(
            1726999658000,
            2000,
            TaskTag.STUDY
        )
        insertSession(session)

        var allSessions = emptyList<Session>()

        getAllSessionsUseCase.invoke().collect {
            allSessions = it
        }

        assertTrue(allSessions.any {
            it.time == session.time &&
                    it.duration == session.duration &&
                    it.taskTag == session.taskTag
        })
    }

}