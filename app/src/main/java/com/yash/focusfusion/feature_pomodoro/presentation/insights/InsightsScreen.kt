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
import com.yash.focusfusion.core.util.getTimeListInFormattedWayWithDuration
import com.yash.focusfusion.core.util.getTotalDurationForDifferentHour
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.ActivityInsightCard
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabs
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimeRange
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.WaveLineChartWithAxes

val hoursWorked = listOf(10f, 12f, 20f, 5f, 0f, 4f, 17f)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen(
    insightsViewModel: InsightsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val sessionState by insightsViewModel.sessionListState.collectAsState()

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
                            insightsViewModel.onEvent(InsightsEvent.DayEvent(1726247332000))
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
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Hours Data:- $hourDataList")
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Total Duration For Each hour:- $getTotalDurationForDiffHours"
                            )
                        }

                        1 -> {
                            insightsViewModel.onEvent(
                                InsightsEvent.WeekEvent(
                                    "09",
                                    "15",
                                    "09",
                                    "2024"
                                )
                            )
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Week:- ${sessionState}"
                            )
                            val timeListInFormattedWay =
                                getTimeListInFormattedWayWithDuration(sessionState)
                            val hourDataList =
                                extractHourDataFromDateTimeListWithDuration(timeListInFormattedWay)
                            val getTotalDurationForDiffHours =
                                getTotalDurationForDifferentHour(hourDataList)
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Time Data:- $timeListInFormattedWay")
                            Log.d(INSIGHTSVIEWMODELCHECKING, "Hours Data:- $hourDataList")
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "Total Duration For Each hour:- $getTotalDurationForDiffHours"
                            )
                        }

                        2 -> {
                            insightsViewModel.onEvent(InsightsEvent.MonthEvent("08", "2024"))
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Month:- ${sessionState}"
                            )
                        }

                        3 -> {
                            insightsViewModel.onEvent(InsightsEvent.YearEvent("2024"))
                            Log.d(
                                INSIGHTSVIEWMODELCHECKING,
                                "ALl session for Year:- ${sessionState}"
                            )
                        }
                    }
                    Log.d(INSIGHTSVIEWMODELCHECKING, "Time period selected: $timePeriod")

                }
            }

            item {
                WaveLineChartWithAxes(
                    hoursData = hoursWorked,
                    timeRange = TimeRange.Week,
                    daysInMonth = 30,
                    month = 9,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp),
                    lineColor = Color(0xff9463ED),
                    strokeWidth = 9f,
                    xOffset = 90f,
                    waveAmplitude = 1f
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