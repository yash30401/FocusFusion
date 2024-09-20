package com.yash.focusfusion.core.util

import java.util.LinkedHashMap

fun getTotalDurationWeeklyUsingHashMap(extractedListOfWeekWithDuration: List<Pair<String, Int>>): HashMap<String, Int> {

    val weeksHashMap = HashMap<String, Int>()
    weeksHashMap.put("MONDAY", 0)
    weeksHashMap.put("TUESDAY", 0)
    weeksHashMap.put("WEDNESDAY", 0)
    weeksHashMap.put("THURSDAY", 0)
    weeksHashMap.put("FRIDAY", 0)
    weeksHashMap.put("SATURDAY", 0)
    weeksHashMap.put("SUNDAY", 0)

    for (i in 0..extractedListOfWeekWithDuration.size-1) {
        val key = extractedListOfWeekWithDuration[i].first
        if(weeksHashMap.containsKey(key)){
            val currentDuration = weeksHashMap.getValue(key)
            val addDuration =extractedListOfWeekWithDuration[i].second + currentDuration
            weeksHashMap.put(key, addDuration)
        } else {
            weeksHashMap.put(key, extractedListOfWeekWithDuration[i].second)
        }
    }
    val weekOrder = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
    val sortedMap = weeksHashMap.toList()
        .sortedBy { (key, _) -> weekOrder.indexOf(key) }
        .toMap(LinkedHashMap())

    return sortedMap
}