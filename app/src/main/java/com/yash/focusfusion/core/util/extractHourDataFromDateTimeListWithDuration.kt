package com.yash.focusfusion.core.util

fun extractHourDataFromDateTimeListWithDuration(sessionDateTime: List<Pair<String, Int>>): List<Pair<String, Int>> {
    return sessionDateTime.map {
        it.first.substring(11,13) to it.second
    }
}