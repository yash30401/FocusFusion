package com.yash.focusfusion.feature_pomodoro.presentation.insights

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yash.focusfusion.R
import com.yash.focusfusion.core.util.Constants.INSIGHTSVIEWMODELCHECKING
import com.yash.focusfusion.core.util.extractHourDataFromDateTimeListWithDuration
import com.yash.focusfusion.core.util.getExtractedListOfDatesHashMap
import com.yash.focusfusion.core.util.getExtractedListOfMonthsHashMap
import com.yash.focusfusion.core.util.getListOfDatesNameWithDuration
import com.yash.focusfusion.core.util.getListOfMonthsNameWithDuration
import com.yash.focusfusion.core.util.getListOfWeeksNameWithDuration
import com.yash.focusfusion.core.util.getTimeListInFormattedWayWithDuration
import com.yash.focusfusion.core.util.getTotalDurationForDifferentHour
import com.yash.focusfusion.core.util.getTotalDurationWeeklyUsingHashMap
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.ActivityInsightCard
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabs
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimeRange
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.WaveLineChartWithAxes
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.concurrent.TimeUnit


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen(
    insightsViewModel: InsightsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val sessionState by insightsViewModel.sessionListState.collectAsState()
    val totalDurationState by insightsViewModel.totalDurationState

    var currentTimePeriodTab by remember {
        mutableStateOf(TimeRange.Day)
    }
    val todaysDate = LocalDate.now()

    var currentWeekRange by remember {
        mutableStateOf(
            todaysDate.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            ).dayOfMonth.toString() + "-" +
                    todaysDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).dayOfMonth.toString()

        )
    }
    var monthSelectedFromWeekRange by remember {
        mutableStateOf(
            if (todaysDate.month.value < 10) "0" + todaysDate.month.value.toString() else
                todaysDate.month.value.toString()
        )
    }
    var yearSelectedFromWeekRange by remember {
        mutableStateOf(todaysDate.year.toString())
    }

    var currentMonthForMonthData by remember {
        mutableStateOf(
            if (todaysDate.month.value < 10) "0" + todaysDate.month.value.toString() else
                todaysDate.month.value.toString()
        )
    }

    var currentYearSelectedForMonthData by remember {
        mutableStateOf(todaysDate.year.toString())
    }

    var currentYear by remember {
        mutableStateOf(todaysDate.year.toString())
    }

    var minutesFocused by remember { mutableStateOf<List<Float>>(emptyList()) }

    var overallTotalDuration by remember {
        mutableStateOf(0)
    }

    val dateFormat = SimpleDateFormat("dd,MMM yyyy")
    var date: Date? by remember {
        mutableStateOf(
            dateFormat.parse(
                todaysDate.format(DateTimeFormatter.ofPattern("dd,MMM yyyy"))
            )
        )
    }

    Scaffold(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 50.dp, bottom = 40.dp)
        ) {

            item {
                TimePeriodTabs { timePeriod ->
                    when (timePeriod) {
                        0 -> {
                            currentTimePeriodTab = TimeRange.Day

                            val millis = date?.time
                            println("millis:- $millis")
                            insightsViewModel.onEvent(InsightsEvent.DayEvent(millis ?: 0))

                            overallTotalDuration =
                                TimeUnit.SECONDS.toMinutes(totalDurationState.toLong()).toInt()

                            println("Total Duration:- $overallTotalDuration")
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for date:- ${sessionState}"
                            )
                            val timeListInFormattedWay =
                                getTimeListInFormattedWayWithDuration(sessionState)
                            val hourDataList =
                                extractHourDataFromDateTimeListWithDuration(timeListInFormattedWay)
                            val getTotalDurationForDiffHours =
                                getTotalDurationForDifferentHour(hourDataList)


                            minutesFocused = (0..24).map { i ->
                                if (getTotalDurationForDiffHours.containsKey(i)) {
                                    val secondsToMinutes = TimeUnit.SECONDS.toMinutes(
                                        getTotalDurationForDiffHours.getValue(i).toLong()
                                    )
                                    secondsToMinutes.toFloat()
                                } else {
                                    0f
                                }
                            }


                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Hours Data:- $hourDataList")
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Total Duration For Each hour:- $getTotalDurationForDiffHours"
                            )
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Final List:- $minutesFocused")
                        }

                        1 -> {
                            currentTimePeriodTab = TimeRange.Week

                            val listOfWeekRange = currentWeekRange.split('-')
                            println(listOfWeekRange)

                            val startDate =
                                if (listOfWeekRange[0].toInt() < 10) "0${listOfWeekRange.get(0)}" else listOfWeekRange[0]
                            println(startDate)

                            val endWeek =
                                if (listOfWeekRange[1].toInt() < 10) "0${listOfWeekRange.get(1)}" else listOfWeekRange[1]
                            println(endWeek)

                            insightsViewModel.onEvent(
                                InsightsEvent.WeekEvent(
                                    startDate,
                                    endWeek,
                                    monthSelectedFromWeekRange,
                                    yearSelectedFromWeekRange
                                )
                            )
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Week:- ${sessionState}"
                            )
                            val timeListInFormattedWay =
                                getTimeListInFormattedWayWithDuration(sessionState)

                            val extractedListOfWeekWithDuration = getListOfWeeksNameWithDuration(
                                timeListInFormattedWay
                            )
                            val totalDurationData =
                                getTotalDurationWeeklyUsingHashMap(extractedListOfWeekWithDuration)

                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Weeks Data:- $extractedListOfWeekWithDuration"
                            )
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Total Duration Weekly:- $totalDurationData"
                            )

                            minutesFocused = totalDurationData.map {
                                TimeUnit.SECONDS.toMinutes(it.value.toLong()).toFloat()
                            }
                        }

                        2 -> {
                            currentTimePeriodTab = TimeRange.Month
                            insightsViewModel.onEvent(
                                InsightsEvent.MonthEvent(
                                    currentMonthForMonthData,
                                    currentYearSelectedForMonthData
                                )
                            )

                            val timeListInFormattedWay =
                                getTimeListInFormattedWayWithDuration(sessionState)

                            val extractedListOfDatesWithDuration = getListOfDatesNameWithDuration(
                                timeListInFormattedWay
                            )

                            val extractedListOfDatesHashMap =
                                getExtractedListOfDatesHashMap(
                                    extractedListOfDatesWithDuration,
                                    currentMonthForMonthData.length
                                )

                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Month:- ${sessionState}"
                            )
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")

                            minutesFocused = extractedListOfDatesHashMap.map {
                                TimeUnit.SECONDS.toMinutes(it.value.toLong()).toFloat()
                            }
                        }

                        3 -> {
                            currentTimePeriodTab = TimeRange.Year
                            insightsViewModel.onEvent(InsightsEvent.YearEvent(currentYear))
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Year:- ${sessionState}"
                            )

                            val timeListInFormattedWay =
                                getTimeListInFormattedWayWithDuration(sessionState)


                            val extractedListOfDatesWithDuration = getListOfMonthsNameWithDuration(
                                timeListInFormattedWay
                            )

                            val extractedListOfMonthsHashMap =
                                getExtractedListOfMonthsHashMap(
                                    extractedListOfDatesWithDuration
                                )

                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")

                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Years Data:- $extractedListOfDatesWithDuration"
                            )

                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Sorted Map:- $extractedListOfMonthsHashMap"
                            )

                            minutesFocused = extractedListOfMonthsHashMap.map {
                                TimeUnit.SECONDS.toMinutes(it.value.toLong()).toFloat()
                            }
                        }
                    }
                    Log.d(INSIGHTSVIEWMODELCHECKING, "Time period selected: $timePeriod")

                }
            }

            item {
                WaveLineChartWithAxes(
                    minutesData = minutesFocused,
                    timeRange = currentTimePeriodTab,
                    overallTotalDurationInMinutes = overallTotalDuration,
                    modifier = Modifier
                        .padding(16.dp),
                    lineColor = Color(0xff9463ED),
                    strokeWidth = 5f,
                    xOffset = 90f,
                    waveAmplitude = 1f,
                    onPreviousClick = { dateOrRange, month, year ->
                        when (currentTimePeriodTab) {
                            TimeRange.Day -> {
                                date = dateFormat.parse(dateOrRange)
                            }

                            TimeRange.Week -> {
                                currentWeekRange = dateOrRange!!
                                monthSelectedFromWeekRange = month!!
                                yearSelectedFromWeekRange = year!!
                            }

                            TimeRange.Month -> {
                                currentMonthForMonthData = month!!
                                currentYearSelectedForMonthData = year!!
                            }

                            TimeRange.Year -> {
                                currentYear = year!!
                            }
                        }

                    },
                    onNextClick = { dateOrRange, month, year ->
                        when (currentTimePeriodTab) {
                            TimeRange.Day -> {
                                date = dateFormat.parse(dateOrRange)
                            }

                            TimeRange.Week -> {
                                currentWeekRange = dateOrRange!!
                                monthSelectedFromWeekRange = month!!
                                yearSelectedFromWeekRange = year!!
                            }

                            TimeRange.Month -> {
                                currentMonthForMonthData = month!!
                                currentYearSelectedForMonthData = year!!
                            }

                            TimeRange.Year -> {
                                currentYear = year!!
                            }
                        }
                    }
                )
            }

            item {
                Text(
                    "Activities",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(vertical = 5.dp),
                    fontFamily = FontFamily(Font(R.font.jost_medium)),
                    fontSize = 30.sp,
                    color = Color(0xff212121)
                )
            }

            items(getActivityItemListData()) { activity ->
                ActivityInsightCard(activity.icon, activity.taskTag, activity.totalTimeInMinutes)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InsightsScreenPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xffFFFDFC)
            )
            .padding(10.dp)
    ) {
//        InsightsScreen()
    }
}

data class activityItemList(
    @DrawableRes val icon: Int,
    val taskTag: TaskTag,
    val totalTimeInMinutes: Int
)

fun getActivityItemListData(): List<activityItemList> {
    return listOf(
        activityItemList(R.drawable.books, TaskTag.STUDY, 210),
        activityItemList(R.drawable.person_with_ball, TaskTag.SPORT, 10),
        activityItemList(R.drawable.sleeping_accommodation, TaskTag.RELAX, 140),
        activityItemList(R.drawable.person_doing_cartwheel, TaskTag.EXERCISE, 2370),
        activityItemList(R.drawable.books, TaskTag.STUDY, 61),
        activityItemList(R.drawable.person_with_ball, TaskTag.SPORT, 10),
        activityItemList(R.drawable.sleeping_accommodation, TaskTag.RELAX, 140),
    )
}