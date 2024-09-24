package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import java.util.Locale

@Composable
fun ActivityInsightCard(
    @DrawableRes icon: Int,
    taskTag: TaskTag,
    totalMinutesForActivity: Int,
    modifier: Modifier = Modifier
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
            .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp))
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
                        Color(0xffF0F0F0),
                        RoundedCornerShape(100.dp)
                    )
                    .padding(5.dp)
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(icon), "",
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = "${
                    if (taskTag == TaskTag.ENTERTAINMENT) taskTag.name.toLowerCase(Locale.getDefault())
                        .replaceFirstChar { it.uppercase() }.dropLast(4)+"..." else
                        taskTag.name.toLowerCase(Locale.getDefault())
                            .replaceFirstChar { it.uppercase() }
                }",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
                fontSize = 17.sp,
                fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                color = Color(0xff212121),
                textAlign = TextAlign.Center,
            )
        }

        VerticalDivider(modifier = Modifier.height(40.dp), thickness = 1.3.dp)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp,
                        color = Color(0xff212121)
                    )
                ) {
                    append(hours.toString())
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp, color = Color(0xffFF8D61)
                    )
                ) {
                    append(" Hrs")
                }

                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp,
                        color = Color(0xff212121)
                    )
                ) {
                    append(" " + minutes.toString())
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 20.sp, color = Color(0xffFF8D61)
                    )
                ) {
                    append(" min")
                }
            })
        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ActivityInsightCardPreview() {
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