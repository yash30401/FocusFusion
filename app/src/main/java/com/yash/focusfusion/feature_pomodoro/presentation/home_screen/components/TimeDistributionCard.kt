package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import java.util.concurrent.TimeUnit

@Composable
fun TimeDistributionCard(
    @DrawableRes icon: Int,
    task: TaskTag,
    totalTimeInMinutes: Int,
    totalTimeInMinutesLastWeek: Int,
    modifier: Modifier = Modifier,
) {

    val progress by remember {
        mutableStateOf(
            if (totalTimeInMinutesLastWeek == 0) 100f
            else (totalTimeInMinutes * 100 / totalTimeInMinutesLastWeek) / 100f
        )
    }
    Log.d("PROGRESS",progress.toString())

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .shadow(2.dp, shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(100.dp)
                    )
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "$task",
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                text = "$task",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        LinearProgressIndicator(
            modifier = Modifier
                .height(28.dp)
                .width(150.dp)
                .padding(vertical = 10.dp),
            progress = { progress },
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Text(
            text = if (totalTimeInMinutes > 60) TimeUnit.MINUTES
                .toHours(totalTimeInMinutes.toLong())
                .toString() + "hrs" else totalTimeInMinutes.toString() + "mins",

            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Centers text within the Column
                .wrapContentWidth(Alignment.CenterHorizontally), // Ensures it doesn’t stretch
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimeDistributionCardPreview() {
    FocusFusionTheme {
        TimeDistributionCard(
            R.drawable.books,
            TaskTag.STUDY,
            8,
            92,
            modifier = Modifier.padding(10.dp)
        )
    }
}