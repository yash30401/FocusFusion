package com.yash.focusfusion.feature_pomodoro.presentation.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@Composable
fun InsightsScreen(modifier: Modifier = Modifier) {
    timePeriodTabs() {

    }
}

@Composable
fun timePeriodTabs(modifier: Modifier = Modifier, selectedPeriod: (Int) -> Unit) {

    val currentSelectedTimePeriod by remember {
        mutableStateOf(0)
    }
    selectedPeriod(currentSelectedTimePeriod)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 48.dp
            )
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(Color.Cyan, RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier =
            if (currentSelectedTimePeriod == 0) {
                Modifier
                    .background(Color(0xFFFF8D61), RoundedCornerShape(20.dp))
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            } else {
                Modifier
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            }
        ) {
            Text(
                "Today",
                fontSize = 13.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
            )
        }



        Column(
            modifier =
            if (currentSelectedTimePeriod == 1) {
                Modifier
                    .background(Color(0xFFFF8D61))
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            } else {
                Modifier
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            }
        ) {
            Text(
                "Week",
                fontSize = 13.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
            )
        }

        Column(
            modifier =
            if (currentSelectedTimePeriod == 2) {
                Modifier
                    .background(Color(0xFFFF8D61))
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            } else {
                Modifier
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            }
        ) {
            Text(
                "Month",
                fontSize = 13.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
            )
        }
        Column(
            modifier =
            if (currentSelectedTimePeriod == 3) {
                Modifier
                    .background(Color(0xFFFF8D61))
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            } else {
                Modifier
                    .padding(
                        horizontal = 12.dp
                    )
                    .padding(7.dp)
            }
        ) {
            Text(
                "Year",
                fontSize = 13.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InsightsScreenPreview() {
    Column(modifier = Modifier.padding(10.dp)) {
        InsightsScreen()
    }
}