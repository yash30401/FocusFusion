package com.yash.focusfusion.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getListOfWeeksNameWithDuration(list: List<Pair<String, Int>>): List<Pair<String, Int>> {
    return list.map {
        LocalDate.parse(
            it.first,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ).dayOfWeek.toString() to it.second
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getListOfDatesNameWithDuration(list: List<Pair<String, Int>>): List<Pair<String, Int>> {
    return list.map {
        LocalDate.parse(
            it.first,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ).dayOfMonth.toString() to it.second
    }
}