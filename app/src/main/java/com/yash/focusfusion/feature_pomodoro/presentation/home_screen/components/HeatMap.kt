package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yash.focusfusion.core.util.generateDayBoxes
import com.yash.focusfusion.feature_pomodoro.domain.model.DayBox
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import java.time.LocalDate

@Composable
fun HeatMap(
    weeks: List<List<DayBox>>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary,
                RoundedCornerShape(20.dp))
            .padding(horizontal = 15.dp, vertical = 15.dp)

    ) {
        items(weeks.size) { weekIndex ->
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .padding(horizontal = 1.dp)
            ) {
                weeks[weekIndex].forEach { dayBox ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (dayBox.hasSession) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                    alpha = .45f
                                )
                            )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HeatMapPreview() {
    val sessionDates = setOf(
        LocalDate.of(2024, 1, 2),
        LocalDate.of(2025, 2, 23),
        LocalDate.of(2025, 3, 19),
        LocalDate.of(2025, 3, 22),
        LocalDate.of(2025, 4, 22),
        LocalDate.of(2025, 5, 1),
        LocalDate.of(2025, 6, 20),
        LocalDate.of(2025, 7, 28),
        LocalDate.now()
    )

    val weeks = generateDayBoxes(sessionDates)
    val scrollsState = rememberLazyListState()
    FocusFusionTheme {
        HeatMap(weeks, scrollsState)
    }
}