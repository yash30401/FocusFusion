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
    fun getSessionById(id: Int): SessionEntity?

    @Query("select * from sessions")
    fun getAllSessions(): Flow<List<SessionEntity>>

    // Aggregated Queries

    @Query("SELECT SUM(duration) FROM sessions WHERE DATE(time / 1000, 'unixepoch', 'localtime') = DATE(:date / 1000, 'unixepoch', 'localtime')")
    fun getTotalSecondsForDate(date: Long): Int

    @Query(
        """
    SELECT SUM(duration) FROM sessions
    WHERE time BETWEEN :startTimestamp AND :endTimestamp
"""
    )
    fun getTotalSecondsForWeek(startTimestamp: Long,endTimestamp: Long): Int

    @Query(
        "SELECT SUM(duration) FROM sessions\n" +
            "WHERE strftime('%m', time / 1000, 'unixepoch', 'localtime') = :month\n" +
            "AND strftime('%Y', time / 1000, 'unixepoch', 'localtime') = :year"
    )
    fun getTotalSecondsForMonth(month: String, year: String): Int

    @Query("SELECT SUM(duration) FROM sessions WHERE strftime('%Y', time / 1000, 'unixepoch', 'localtime') = :year")
    fun getTotalSecondsForYear(year: String): Int

    @Query("SELECT * FROM sessions WHERE DATE(time / 1000, 'unixepoch', 'localtime') = DATE(:date / 1000, 'unixepoch', 'localtime')")
    fun getSessionsForDate(date: Long): Flow<List<SessionEntity>>

//    @Query(
//        "SELECT * FROM sessions\n" +
//            "WHERE strftime('%d', time / 1000, 'unixepoch', 'localtime') BETWEEN :startDay AND :endDay\n" +
//            "AND strftime('%m', time / 1000, 'unixepoch', 'localtime') = :month\n" +
//            "AND strftime('%Y', time / 1000, 'unixepoch', 'localtime') = :year\n"
//    )
//    fun getSessionsForWeek(
//        startDay: String,
//        endDay: String,
//        month: String,
//        year: String,
//    ): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE time BETWEEN :startTimestamp AND :endTimestamp")
    fun getSessionsForWeek(
        startTimestamp: Long,
        endTimestamp: Long,
    ): Flow<List<SessionEntity>>


    @Query(
        "SELECT * FROM sessions\n" +
            "WHERE strftime('%m', time / 1000, 'unixepoch', 'localtime') = :month\n" +
            "AND strftime('%Y', time / 1000, 'unixepoch', 'localtime') = :year\n"
    )
    fun getSessionsForMonth(month: String, year: String): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE strftime('%Y', time / 1000, 'unixepoch', 'localtime') = :year\n")
    fun getSessionsForYear(year: String): Flow<List<SessionEntity>>
}