package com.yash.focusfusion.feature_pomodoro.domain.repository

import androidx.room.Query
import com.yash.focusfusion.feature_pomodoro.data.local.entity.SessionEntity
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository{
    suspend fun insertSession(session: Session)
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(session: Session)
    suspend fun getSessionById(id:Int):Session?
    fun getAllSessions(): Flow<List<Session>>
    suspend fun getTotalMinutesForDate(date:Long):Int
    suspend fun getTotalMinutesForMonth(month:String,year:String):Int
    suspend fun getTotalMinutesForYear(year: String): Int
    fun getSessionsForDate(date: Long): Flow<List<Session>>
    fun getSessionsForMonth(month: String, year: String): Flow<List<Session>>
    fun getSessionsForYear(year: String): Flow<List<Session>>

}