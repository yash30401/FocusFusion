package com.yash.focusfusion.feature_pomodoro.presentation.insights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabs

@Composable
fun InsightsScreen(modifier: Modifier = Modifier) {
    TimePeriodTabs {

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InsightsScreenPreview() {
    Column(modifier = Modifier.padding(10.dp)) {
        InsightsScreen()
    }
}