package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.yash.focusfusion.core.util.getListOfWeeksNameWithDuration
import com.yash.focusfusion.core.util.getTimeListInFormattedWayWithDuration
import com.yash.focusfusion.core.util.getTotalDurationWeeklyUsingHashMap
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.GreetingHead
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.HomeScreenWaveLineChart
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeDistributionCard
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeRange
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val weeklySessionState by homeScreenViewModel.weeklySessions.collectAsState()
    var minutesFocused by remember { mutableStateOf<List<Float>>(emptyList()) }

    val todaysDate = LocalDate.now()

    val dateFormat = SimpleDateFormat("dd,MMM yyyy")
    var date: Date? by remember {
        mutableStateOf(
            dateFormat.parse(
                todaysDate.format(DateTimeFormatter.ofPattern("dd,MMM yyyy"))
            )
        )
    }

    var currentWeekRange by remember {
        mutableStateOf(
            todaysDate.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            ).dayOfMonth.toString() + "-" +
                todaysDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).dayOfMonth.toString()

        )
    }

    val listOfWeekRange = currentWeekRange.split('-')
    val startDate =
        if (listOfWeekRange[0].toInt() < 10) "0${listOfWeekRange.get(0)}" else listOfWeekRange[0]
    val endWeek =
        if (listOfWeekRange[1].toInt() < 10) "0${listOfWeekRange.get(1)}" else listOfWeekRange[1]

    var currentMonth by remember {
        mutableStateOf(
            if (todaysDate.month.value < 10) "0" + todaysDate.month.value.toString() else
                todaysDate.month.value.toString()
        )
    }
    var currentYear by remember {
        mutableStateOf(todaysDate.year.toString())
    }

    initHomeViewModel(
        homeScreenViewModel,
        listOfWeekRange,
        startDate,
        endWeek,
        currentMonth,
        currentYear
    )

    minutesFocused = getMappedDataForChart(weeklySessionState)


    Column {
        GreetingHead("Yashveer Singh", modifier = Modifier.padding(top = 30.dp))
        HomeScreenWaveLineChart(
            minutesFocused,
            TimeRange.Week,
            modifier = Modifier.padding(16.dp),
            lineColor = Color(0xff9463ED),
            strokeWidth = 5f,
            xOffset = 90f,
            waveAmplitude = 1f,
        )
        Text(
            text = "Time Distribution",
            modifier = Modifier.padding(start = 16.dp, top = 20.dp),
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.jost_medium))
        )
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(10) { item ->
                TimeDistributionCard(
                    R.drawable.books,
                    "Study", 265,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMappedDataForChart(weeklySessionState: List<Session>): List<Float> {
    val timeListInFormattedWay =
        getTimeListInFormattedWayWithDuration(weeklySessionState)
    val extractedListOfWeekWithDuration = getListOfWeeksNameWithDuration(
        timeListInFormattedWay
    )
    val totalDurationData =
        getTotalDurationWeeklyUsingHashMap(extractedListOfWeekWithDuration)

    return totalDurationData.map {
        TimeUnit.SECONDS.toMinutes(it.value.toLong()).toFloat()
    }
}

fun initHomeViewModel(
    homeScreenViewModel: HomeScreenViewModel,
    listOfWeekRange: List<String>,
    startDate: String,
    endWeek: String,
    currentMonth: String,
    currentYear: String,
) {
    homeScreenViewModel.onEvent(
        HomeEvent.WeekEvent(
            startDate,
            endWeek,
            currentMonth,
            currentYear
        )
    )
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen()
//}