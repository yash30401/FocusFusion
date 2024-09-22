package com.yash.focusfusion.core.util

import java.util.SortedMap

fun getExtractedListOfDatesHashMap(list:List<Pair<String,Int>>,month:Int):SortedMap<Int,Int>{

    val hashMap = HashMap<Int,Int>()
    if(month == 30){
        (1..30).forEach {
            hashMap.put(it,0)
        }
    }else{
        (1..31).forEach {
            hashMap.put(it,0)
        }
    }


    for (i in 0..list.size-1) {
        val key = list[i].first.toInt()
        if(hashMap.containsKey(key)){
            val currentDuration = hashMap.getValue(key)
            val addDuration =list[i].second + currentDuration
            hashMap.put(key, addDuration)
        } else {
            hashMap.put(key, list[i].second)
        }
    }
    val sortedMap = hashMap.toSortedMap()

    return sortedMap
}


fun getExtractedListOfMonthsHashMap(list:List<Pair<String,Int>>):SortedMap<Int,Int>{

    val hashMap = HashMap<Int,Int>()
        (1..12).forEach {
            hashMap.put(it,0)
        }

    for (i in 0..list.size-1) {
        val key = list[i].first.toInt()
        if(hashMap.containsKey(key)){
            val currentDuration = hashMap.getValue(key)
            val addDuration =list[i].second + currentDuration
            hashMap.put(key, addDuration)
        } else {
            hashMap.put(key, list[i].second)
        }
    }
    val sortedMap = hashMap.toSortedMap()

    return sortedMap
}