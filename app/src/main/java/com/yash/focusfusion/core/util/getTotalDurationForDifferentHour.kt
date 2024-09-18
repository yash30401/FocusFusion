package com.yash.focusfusion.core.util

fun getTotalDurationForDifferentHour(hourDataList: List<Pair<String, Int>>): HashMap<Int, Int> {
    val totalDurationData = HashMap<Int, Int>()
    hourDataList.forEach {
        val key = it.first.toInt()
        if (totalDurationData.containsKey(key)) {
            val currentDuration = totalDurationData.getValue(key)
            val addDuration = it.second + currentDuration
            totalDurationData.put(key, addDuration)
        } else {
            totalDurationData.put(key, it.second)
        }
    }
    return totalDurationData
}