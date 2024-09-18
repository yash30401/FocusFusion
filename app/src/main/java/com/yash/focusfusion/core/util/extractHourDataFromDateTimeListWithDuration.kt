package com.yash.focusfusion.core.util

fun extractHourDataFromDateTimeListWithDuration(sessionDateTime: List<Pair<String, Int>>): List<Pair<String, Int>> {
    return sessionDateTime.map {
        if (it.first.substring(11, 13).equals("00")) "0" to it.second else
            it.first.substring(11, 13) to it.second
    }
}