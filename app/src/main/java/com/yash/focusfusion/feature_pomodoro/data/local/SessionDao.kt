package com.yash.focusfusion.feature_pomodoro.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yash.focusfusion.feature_pomodoro.data.local.entity.SessionEntity
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: SessionEntity)

    @Update
    fun updateSession(session: SessionEntity)

    @Delete
    fun deleteSession(session: SessionEntity)

    @Query("select * from sessions where id = :id")
    fun getSessionById(id:Int):SessionEntity?

    @Query("select * from sessions")
    fun getAllSessions(): Flow<List<SessionEntity>>

    // Aggregated Queries

    @Query("select sum(duration) from sessions where date(startTime/1000,'unixepoch') = DATE(:date / 1000, 'unixepoch')")
    fun getTotalMinutesForDate(date:Long):Int

    @Query("SELECT SUM(duration) FROM sessions WHERE strftime('%m', startTime / 1000, 'unixepoch') = :month AND strftime('%Y', startTime / 1000, 'unixepoch') = :year")
    fun getTotalMinutesForMonth(month:String,year:String):Int

    @Query("SELECT SUM(duration) FROM sessions WHERE strftime('%Y', startTime / 1000, 'unixepoch') = :year")
    fun getTotalMinutesForYear(year: String): Int

    @Query("SELECT * FROM sessions WHERE DATE(startTime / 1000, 'unixepoch') = DATE(:date / 1000, 'unixepoch')")
    fun getSessionsForDate(date: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE strftime('%m', startTime / 1000, 'unixepoch') = :month AND strftime('%Y', startTime / 1000, 'unixepoch') = :year")
    fun getSessionsForMonth(month: String, year: String): Flow<List<SessionEntity>>

    // Retrieve sessions for a specific year
    @Query("SELECT * FROM sessions WHERE strftime('%Y', startTime / 1000, 'unixepoch') = :year")
    fun getSessionsForYear(year: String): Flow<List<SessionEntity>>
}