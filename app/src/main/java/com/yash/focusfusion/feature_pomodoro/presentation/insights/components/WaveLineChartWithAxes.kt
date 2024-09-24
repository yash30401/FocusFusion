package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.yash.focusfusion.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit

@SuppressLint("UnrememberedAnimatable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaveLineChartWithAxes(
    minutesData: List<Float>,
    timeRange: TimeRange,
    overallTotalDurationInMinutes: Int,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF9463ED),
    strokeWidth: Float = 5f,
    xOffset: Float = 50f,  // Shift the X-axis and wave to the right
    waveAmplitude: Float = 2f, // Amplify the wave effect
    onPreviousClick: (String?, String?, String?) -> Unit,
    onNextClick: (String?, String?, String?) -> Unit,
) {
    val minValue = 0f   // Y-axis min (0 minutes)
    val maxValue =
        (minutesData.maxOrNull() ?: minValue).coerceAtLeast(minValue)  // Y-axis max based on data

    // Decide on number of Y-axis steps (e.g., 4 steps)
    val numberOfSteps = 4
    val yAxisStep =
        if (maxValue - minValue == 0f) 0f else (maxValue - minValue) / numberOfSteps.toFloat()

    //Time Range For Day
    var currentDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formatter = DateTimeFormatter.ofPattern("d,MMM yyyy", Locale.ENGLISH)
    var formattedDate by remember {
        mutableStateOf("")
    }

    val todaysDate = LocalDate.now()
    // TimeRange For Week
    var startOfWeek by remember {
        mutableStateOf(
            todaysDate.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            )
        )
    }
    var endOfWeek by remember {
        mutableStateOf(
            todaysDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        )
    }

    var currentWeekRange by remember {
        mutableStateOf(
            startOfWeek.dayOfMonth.toString() + "-" +
                    endOfWeek.dayOfMonth.toString()
        )
    }

    var monthTemporalAdjuster by remember {
        mutableStateOf(todaysDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
    }
    var currentMonth by remember {
        mutableStateOf(
            todaysDate.month.value.toString()
        )
    }

    var currentMonthInWord by remember {
        mutableStateOf(todaysDate.month.toString())
    }

    var currentMonthDays by remember {
        mutableStateOf(todaysDate.month.maxLength())
    }

    var currentYear by remember {
        mutableStateOf(
            todaysDate.year
        )
    }

    var overallTotalDurationState by remember {
        mutableStateOf(overallTotalDurationInMinutes)
    }

    val pathProgress = remember(minutesData) { Animatable(0f) }
    val waveDropAnimation = remember(minutesData) { Animatable(0f) }

    // Trigger the animation once on composition
    LaunchedEffect(minutesData) {
        pathProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )

        waveDropAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000, easing = LinearEasing)
        )
    }


//    val drawWaveLineAnimation by rememberInfiniteTransition()
//        .animateFloat(
//            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
//                animation = tween(2000, easing = LinearEasing),
//                repeatMode = RepeatMode.Restart
//            )
//        )

    println("Wave Chart Duration:- " + overallTotalDurationState.toString())

    val currentImmutableWeekRange = todaysDate.with(
        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
    ).dayOfMonth.toString() + "-" +
            todaysDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).dayOfMonth.toString()

    when (timeRange) {
        TimeRange.Day -> {
            formattedDate = currentDate.format(formatter)
        }

        TimeRange.Week -> {
            currentWeekRange
        }

        TimeRange.Month -> {
            currentMonth
        }

        TimeRange.Year -> {
            currentYear
        }
    }

    val totalHours = overallTotalDurationInMinutes / 60
    val totalMinutes = overallTotalDurationInMinutes % 60

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {

                    //Previous Date onClick

                    when (timeRange) {
                        TimeRange.Day -> {
                            currentDate = currentDate.minusDays(1)
                            formattedDate = currentDate.format(formatter)

                            onPreviousClick(formattedDate, null, null)

                        }

                        TimeRange.Week -> {
                            startOfWeek = startOfWeek.minusWeeks(1)
                            endOfWeek = endOfWeek.minusWeeks(1)
                            currentWeekRange = startOfWeek.dayOfMonth.toString() + "-" +
                                    endOfWeek.dayOfMonth.toString()

                            currentMonth = "${
                                if (startOfWeek.month.value < 10) "0" + startOfWeek.month.value.toString()
                                else startOfWeek.month.value.toString()
                            }"

                            onPreviousClick(
                                currentWeekRange,
                                currentMonth, currentYear.toString()
                            )

                        }

                        TimeRange.Month -> {
                            currentMonth = "${
                                if (monthTemporalAdjuster.month.value < 10) "0" + monthTemporalAdjuster.month.minus(
                                    1
                                ).value.toString()
                                else monthTemporalAdjuster.month.minus(1).value.toString()
                            }"
                            monthTemporalAdjuster = monthTemporalAdjuster.minusMonths(1)
                            val previousMonth = monthTemporalAdjuster
                            currentMonthInWord = previousMonth.month.toString()
                            currentYear = previousMonth.year

                            onPreviousClick(
                                null, currentMonth,
                                currentYear.toString()
                            )

                        }

                        TimeRange.Year -> {
                            val previousYear = currentYear.minus(1)
                            currentYear = previousYear

                            onPreviousClick(null, null, currentYear.toString())

                        }
                    }


                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Previous",
                        tint = Color(0xff787878)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (timeRange) {
                            TimeRange.Day -> {
                                formattedDate
                            }

                            TimeRange.Week -> {
                                currentWeekRange
                            }

                            TimeRange.Month -> {
                                currentMonthInWord
                            }

                            TimeRange.Year -> {
                                currentYear.toString()
                            }
                        },
                        fontSize = 15.sp,
                        color = Color(0xff787878),
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                                    fontSize = 10.sp,
                                    color = Color(0xffFF8D61),
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(totalHours.toString())
                            }


                            withStyle(
                                style = SpanStyle(
                                    fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                                    fontSize = 10.sp,
                                    color = Color(0xff9E9E9E),
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append(" hrs ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                                    fontSize = 10.sp,
                                    color = Color(0xffFF8D61),
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(totalMinutes.toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                                    fontSize = 10.sp,
                                    color = Color(0xff9E9E9E),
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append(" mins")
                            }

                        }

                    )
                }
                IconButton(onClick = {
                    // Next Date On Click


                    when (timeRange) {
                        TimeRange.Day -> {
                            currentDate =
                                if (currentDate < LocalDate.now()) currentDate.plusDays(1) else currentDate
                            formattedDate = currentDate.format(formatter)
                            onNextClick(formattedDate, null, null)

                        }

                        TimeRange.Week -> {

                            if (currentWeekRange != currentImmutableWeekRange) {
                                startOfWeek = startOfWeek.plusWeeks(1)
                                endOfWeek = endOfWeek.plusWeeks(1)
                                currentWeekRange = startOfWeek.dayOfMonth.toString() + "-" +
                                        endOfWeek.dayOfMonth.toString()

                                currentMonth = "${
                                    if (startOfWeek.month.value < 10) "0" + startOfWeek.month.value.toString()
                                    else startOfWeek.month.value.toString()
                                }"
                                onNextClick(
                                    currentWeekRange,
                                    currentMonth,
                                    currentYear.toString()
                                )

                            }


                        }

                        TimeRange.Month -> {

                            if (currentMonthInWord != startOfWeek.month.toString() || currentYear != startOfWeek.year) {

                                currentMonth = "${
                                    if (monthTemporalAdjuster.month.value < 10) "0" + monthTemporalAdjuster.month.plus(
                                        1
                                    ).value.toString()
                                    else monthTemporalAdjuster.month.plus(1).value.toString()
                                }"

                                monthTemporalAdjuster = monthTemporalAdjuster.plusMonths(1)
                                val nextMonth = monthTemporalAdjuster
                                currentMonthInWord = nextMonth.month.toString()
                                currentYear = nextMonth.year
                            }

                            onNextClick(null, currentMonth, currentYear.toString())

                        }

                        TimeRange.Year -> {
                            if (currentYear != todaysDate.year) {
                                val nextYear = currentYear.plus(1)
                                currentYear = nextYear
                            }
                            onNextClick(null, null, currentYear.toString())

                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "Next",
                        tint = Color(0xff787878)
                    )
                }
            }

            if (minutesData.sum().toInt() == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No Chart Data For This ${timeRange.name}",
                        color = Color(0xff9E9E9E),
                        fontFamily = FontFamily(listOf(Font(R.font.jost_medium))),
                        fontSize = 17.sp
                    )
                }
            }
            // Canvas for the chart
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  // Adjust height for better fit
                    .padding(vertical = 25.dp)
                    .padding(start = 20.dp)
            ) {
                // X-axis labels for different time ranges
                val xAxisLabels = when (timeRange) {
                    TimeRange.Day -> listOf(
                        "00:00", "", "", "", "", "", "06:00",
                        "", "", "", "", "", "12:00",
                        "", "", "", "", "", "18:00",
                        "", "", "", "", "", ""
                    )

                    TimeRange.Week -> listOf("M", "T", "W", "T", "F", "S", "S")
                    TimeRange.Month -> {
                        if (currentMonthDays == 30) {
                            (1..30).map {
                                if (it % 5 == 0) "${currentMonth}/${it}" else ""
                            }
                        } else {
                            (1..31).map {
                                if (it % 5 == 0) "${currentMonth}/${it}" else ""
                            }
                        }
                    }

                    TimeRange.Year -> listOf(
                        "01", "02", "03", "04", "05", "06",
                        "07", "08", "09", "10", "11", "12"
                    )
                }

                val leftPadding = xOffset
                val rightPadding = xOffset
                val widthPerLabel =
                    (size.width - leftPadding - rightPadding) / (xAxisLabels.size - 1)
                val heightPerUnit =
                    if (maxValue - minValue == 0f) 0f else size.height / (maxValue - minValue)

                val textPaint = Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 28f
                    textAlign = Paint.Align.RIGHT
                }


                // Y-axis labels
                for (i in 0..numberOfSteps) {
                    val yLabelValue = minValue + i * yAxisStep
                    val y = size.height - (yLabelValue - minValue) * heightPerUnit
                    drawContext.canvas.nativeCanvas.drawText(
                        String.format("%.0f m", yLabelValue),
                        leftPadding - 10f,
                        y + 10f,  // Adjust for text height
                        textPaint
                    )
                }

                // X-axis labels
                for (i in xAxisLabels.indices) {
                    val x = leftPadding + i * widthPerLabel
                    // Ensure x does not exceed size.width - rightPadding
                    val adjustedX = minOf(x, size.width - rightPadding)
                    drawContext.canvas.nativeCanvas.drawText(
                        xAxisLabels[i],
                        adjustedX,
                        size.height + 40f,  // Adjust padding below the chart
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 28f
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }

                // Create path for the wave line
                val wavePath = Path().apply {
                    val firstX = leftPadding
                    val firstY = size.height - (minutesData[0] - minValue) * heightPerUnit
                    moveTo(firstX, firstY)

                    for (i in 1 until minutesData.size) {
                        val prevX = leftPadding + (i - 1) * widthPerLabel
                        val prevY = size.height - (minutesData[i - 1] - minValue) * heightPerUnit

                        val currX = leftPadding + i * widthPerLabel
                        val currY = size.height - (minutesData[i] - minValue) * heightPerUnit

                        val controlX1 = prevX + widthPerLabel / 2 * waveAmplitude
                        val controlY1 = prevY
                        val controlX2 = currX - widthPerLabel / 2 * waveAmplitude
                        val controlY2 = currY

                        cubicTo(controlX1, controlY1, controlX2, controlY2, currX, currY)
                    }
                }

                val lastX = leftPadding + (minutesData.size - 1) * widthPerLabel

                val fillPath = Path().apply {
                    addPath(wavePath)
                    lineTo(lastX, size.height)  // Line to the bottom of the last data point
                    lineTo(
                        leftPadding,
                        size.height
                    )  // Line back to the starting X position at the bottom
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.5f), Color.Transparent),
                        startY = 0f,
                        endY = size.height
                    ),
                    alpha = waveDropAnimation.value
                )

                // Measure the total length of the path
                val pathMeasure = PathMeasure()
                pathMeasure.setPath(wavePath, false)
                val pathLength = pathMeasure.length

                // Create a segment of the path based on animation progress
                val animatedSegment = Path()
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = pathProgress.value * pathLength,
                    destination = animatedSegment,
                    startWithMoveTo = true
                )

                // Draw the wave line
                drawPath(
                    path = animatedSegment,
                    color = lineColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Draw Y-axis line
                drawLine(
                    color = Color.Gray,
                    start = Offset(leftPadding, 0f),
                    end = Offset(leftPadding, size.height),
                    strokeWidth = 3f
                )

                // Draw X-axis line
                drawLine(
                    color = Color.Gray,
                    start = Offset(leftPadding, size.height),
                    end = Offset(lastX, size.height),  // End at the last data point's X position
                    strokeWidth = 3f
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffFFFDFC)
@Composable
fun PreviewWaveLineChartWithAxes() {
    val randomNumbers = (1..12).map {
        kotlin.random.Random.nextInt(0, 300).toFloat()
    }
    val minutesWorked = randomNumbers



    WaveLineChartWithAxes(
        minutesData = minutesWorked,
        timeRange = TimeRange.Year,
        overallTotalDurationInMinutes = 1321,
        modifier = Modifier
            .padding(16.dp),
        lineColor = Color(0xff9463ED),
        strokeWidth = 5f,
        xOffset = 90f,
        waveAmplitude = 1f,
        onPreviousClick = { dayOrRange, month, year ->

        },
        onNextClick = { dayOrRange, month, year -> }
    )
}

enum class TimeRange {
    Day, Week, Month, Year
}
