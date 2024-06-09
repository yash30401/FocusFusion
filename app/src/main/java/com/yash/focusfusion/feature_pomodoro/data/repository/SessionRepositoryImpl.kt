package com.yash.focusfusion.feature_pomodoro.data.repository

import com.yash.focusfusion.feature_pomodoro.data.local.SessionDao
import com.yash.focusfusion.feature_pomodoro.data.mapper.toSession
import com.yash.focusfusion.feature_pomodoro.data.mapper.toSessionEntity
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val dao: SessionDao
) : SessionRepository {
    override suspend fun insertSession(session: Session) {
        dao.insertSession(session.toSessionEntity())
    }

    override suspend fun updateSession(session: Session) {
        dao.updateSession(session.toSessionEntity())
    }

    override suspend fun deleteSession(session: Session) {
        dao.deleteSession(session.toSessionEntity())
    }

    override suspend fun getSessionById(id: Int): Session? {
        return dao.getSessionById(id)?.toSession()
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return dao.getAllSessions().map { it.map { it.toSession() } }
    }

    override suspend fun getTotalMinutesForDate(date: Long): Int {
        return dao.getTotalMinutesForDate(date)
    }

    override suspend fun getTotalMinutesForMonth(month: String, year: String): Int {
        return dao.getTotalMinutesForMonth(month, year)
    }

    override suspend fun getTotalMinutesForYear(year: String): Int {
        return dao.getTotalMinutesForYear(year)
    }

    override fun getSessionsForDate(date: Long): Flow<List<Session>> {
        return dao.getSessionsForDate(date).map { it.map { it.toSession() } }
    }

    override fun getSessionsForMonth(month: String, year: String): Flow<List<Session>> {
        return dao.getSessionsForMonth(month, year).map { it.map { it.toSession() } }
    }

    override fun getSessionsForYear(year: String): Flow<List<Session>> {
        return dao.getSessionsForYear(year).map { it.map { it.toSession() } }
    }
}