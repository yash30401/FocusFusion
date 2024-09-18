package com.yash.focusfusion.feature_pomodoro.presentation.insights

import android.os.Build
import androidx.annotation.RequiresApi
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeListInFormattedWay(sessions: List<Session>): List<String> {
    val list = mutableListOf<String>()
    sessions.forEach {
        val instant = Instant.ofEpochMilli(it.time)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        list.add(formatter.format(instant))
    }
    return list
}