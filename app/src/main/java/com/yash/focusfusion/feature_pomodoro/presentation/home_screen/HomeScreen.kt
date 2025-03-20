package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.GreetingHead
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.HomeScreenWaveLineChart
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.HomeScreenWaveLineChartPreview
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeDistributionCard
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeRange

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    Column {
        GreetingHead("Yashveer Singh", modifier = Modifier.padding(top = 30.dp))
        HomeScreenWaveLineChart(
            listOf(60f, 125f, 60f, 10f, 10f),
            TimeRange.Week,
            265,
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
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}