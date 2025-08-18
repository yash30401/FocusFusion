package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import java.util.Locale

@Composable
fun ActivityInsightCard(
    @DrawableRes icon: Int,
    taskTag: TaskTag,
    totalMinutesForActivity: Int,
    modifier: Modifier = Modifier,
) {
    val timeInMinutesState by remember {
        mutableStateOf(totalMinutesForActivity)
    }
    println("TIME IN MINUTES CARD:- " + timeInMinutesState)
    val hours = timeInMinutesState / 60
    val minutes = timeInMinutesState % 60

    Row(
        modifier = modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .fillMaxWidth()
            .shadow(2.dp, shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f), horizontalArrangement =
            Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(100.dp)
                    )
                    .padding(5.dp)
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(icon),
                    "",
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = "${
                    if (taskTag == TaskTag.ENTERTAINMENT) taskTag.name
                        .toLowerCase(Locale.getDefault())
                        .replaceFirstChar { it.uppercase() }.dropLast(4) + "..." else
                        taskTag.name.toLowerCase(Locale.getDefault())
                            .replaceFirstChar { it.uppercase() }
                }",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
                fontSize = 17.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }

        Divider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append(hours.toString())
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(" Hrs")
                }

                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append(" " + minutes.toString())
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(" min")
                }
            })
        }

    }

}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActivityInsightCardPreview() {
    FocusFusionTheme {
        Column {
            ActivityInsightCard(
                R.drawable.books,
                TaskTag.STUDY,
                130
            )
            ActivityInsightCard(
                R.drawable.books,
                TaskTag.EXERCISE,
                130
            )
            ActivityInsightCard(
                R.drawable.books,
                TaskTag.ENTERTAINMENT,
                130
            )
        }
    }

}