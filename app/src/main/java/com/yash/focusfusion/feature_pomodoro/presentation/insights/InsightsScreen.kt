package com.yash.focusfusion.feature_pomodoro.presentation.insights

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.ActivityInsightCard
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabs
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimeRange
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.WaveLineChartWithAxes

val hoursWorked = listOf(10f, 12f, 20f, 5f, 0f, 4f, 17f)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 30.dp, bottom = 40.dp)
        ) {

            item {
                TimePeriodTabs {

                }
            }

            item {
                WaveLineChartWithAxes(
                    hoursData = hoursWorked,
                    maxHour = 30,
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
                    fontSize = 30.sp
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
        InsightsScreen()
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