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

    @Query("SELECT SUM(duration) FROM sessions WHERE date(time/1000,'unixepoch') = DATE(:date / 1000, 'unixepoch')")
    fun getTotalSecondsForDate(date: Long): Int

    @Query("SELECT SUM(duration) FROM sessions WHERE strftime('%m', time / 1000, 'unixepoch') = :month AND strftime('%Y', time / 1000, 'unixepoch') = :year")
    fun getTotalSecondsForMonth(month: String, year: String): Int

    @Query("SELECT SUM(duration) FROM sessions WHERE strftime('%Y', time / 1000, 'unixepoch') = :year")
    fun getTotalSecondsForYear(year: String): Int

    @Query("SELECT * FROM sessions WHERE DATE(time / 1000, 'unixepoch') = DATE(:date / 1000, 'unixepoch')")
    fun getSessionsForDate(date: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE strftime('%m', time / 1000, 'unixepoch') = :month AND strftime('%Y', time / 1000, 'unixepoch') = :year")
    fun getSessionsForMonth(month: String, year: String): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE strftime('%Y', time / 1000, 'unixepoch') = :year")
    fun getSessionsForYear(year: String): Flow<List<SessionEntity>>
}