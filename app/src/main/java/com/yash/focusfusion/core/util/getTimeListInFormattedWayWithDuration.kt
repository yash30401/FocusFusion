package com.yash.focusfusion.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeListInFormattedWayWithDuration(sessions: List<Session>): List<Pair<String, Int>> {
    val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())

    return sessions.map { session ->
        val instant = Instant.ofEpochMilli(session.time)
        formatter.format(instant) to session.duration
    }
}