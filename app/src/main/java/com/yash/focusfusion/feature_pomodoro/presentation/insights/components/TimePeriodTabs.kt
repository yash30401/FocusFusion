package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsScreen

@Composable
fun TimePeriodTabs(modifier: Modifier = Modifier, selectedPeriod: (Int) -> Unit) {

    var currentSelectedTimePeriod by remember {
        mutableStateOf(0)
    }
    selectedPeriod(currentSelectedTimePeriod)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(horizontal = 40.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimePeriodTabItem(
            isSelected = currentSelectedTimePeriod == 0,
            text = "Day",
            onClick = { currentSelectedTimePeriod = 0 },
            modifier = Modifier.weight(1f) // Apply weight here in Row scope
        )
        TimePeriodTabItem(
            isSelected = currentSelectedTimePeriod == 1,
            text = "Week",
            onClick = { currentSelectedTimePeriod = 1 },
            modifier = Modifier.weight(1f)
        )
        TimePeriodTabItem(
            isSelected = currentSelectedTimePeriod == 2,
            text = "Month",
            onClick = { currentSelectedTimePeriod = 2 },
            modifier = Modifier.weight(1f)
        )
        TimePeriodTabItem(
            isSelected = currentSelectedTimePeriod == 3,
            text = "Year",
            onClick = { currentSelectedTimePeriod = 3 },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimePeriodTabItem(isSelected: Boolean, text: String, onClick: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .shadow(if (isSelected) 1.dp else 0.dp, RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFFFF8D61) else Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(7.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            color = if (isSelected) Color.White else Color(0xff9E9E9E),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimePeriodTabsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffFFFDFC))
            .padding(10.dp)
    ) {
        TimePeriodTabs { }
    }
}