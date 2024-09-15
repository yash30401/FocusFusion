package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
@Composable
fun WaveLineChartWithAxes(
    hoursData: List<Float>,
    maxHour: Int,
    timeRange: TimeRange,
    daysInMonth: Int,
    month: Int,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF9463ED),
    strokeWidth: Float = 4f,
    xOffset: Float = 50f,  // Shift the X-axis and wave to the right
    waveAmplitude: Float = 2f  // Amplify the wave effect
) {
    val maxValue = maxHour.toFloat()  // Y-axis max (12 hours)
    val minValue = 0f   // Y-axis min (0 hours)
    val yAxisStep = calculateTimeDifference(maxHour).toFloat()  // Y-axis step (3-hour intervals)

    Box(
        modifier = modifier
            .shadow(2.dp, shape = RoundedCornerShape(20.dp))
            .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp))
            .padding(bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {


        Column(
            modifier = Modifier
                .size(450.dp)
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
              ) {
                IconButton(onClick = {},) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew,"Previous",
                        tint = Color(0xff787878))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,

                    ) {
                    Text(
                        "3-9 June",
                        fontSize = 15.sp,
                        color = Color(0xff787878),
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                    )

                    Text(
                        "53 Hrs",
                        fontSize = 10.sp,
                        color = Color(0xff9E9E9E),
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                    )
                }
                IconButton(onClick = {},) {
                    Icon(imageVector = Icons.Default.ArrowForwardIos,"Previous",
                        tint = Color(0xff787878))
                }
            }
            Canvas(
                modifier = Modifier
                    .size(290.dp)
                    .padding(top = 30.dp)
            ) {

                val xAxisLabels = when (timeRange) {
                    TimeRange.Today -> listOf("12 AM", "6 AM", "12 PM", "6 PM")
                    TimeRange.Week -> listOf("M", "T", "W", "T", "F", "S", "S")
                    TimeRange.Month -> {
                        if (daysInMonth == 30) {
                            listOf("${month}/1", "${month}/8", "${month}/16", "${month}/23", "9/30")
                        } else {
                            listOf(
                                "${month}/1",
                                "${month}/9",
                                "${month}/16",
                                "${month}/24",
                                "${month}/31"
                            )
                        }
                    }

                    TimeRange.Year -> listOf(
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12"
                    )
                }

                val widthPerDay = (size.width - xOffset) / (xAxisLabels.size - 1)
                val yAxisGap = size.height / ((maxValue - minValue) / yAxisStep)

                val textPaint = Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 28f
                    textAlign = Paint.Align.RIGHT
                }

                val textBaselineOffset = textPaint.descent() // Offset to align text baselines

                // Calculate positions for the axes
                val xAxisStart = Offset(xOffset - 35f, size.height - 10f)
                val yAxisStart = Offset(xOffset - 35f, 0f - 125f)
                val xAxisEnd = Offset(size.width + 20f, size.height - 10f)
                val yAxisEnd = Offset(xOffset - 35f, size.height - 10f)

                // Draw Y-axis grid lines and labels (0h, 3h, 6h, 9h, 12h)
                for (i in 0..4) {
                    val y = size.height - (i * yAxisGap)
                    drawContext.canvas.nativeCanvas.drawText(
                        "${i * yAxisStep.toInt()}h",
                        30f,
                        y - 20f,
                        textPaint
                    )
                }

                // Draw X-axis labels (Days of the week) with right shift (xOffset)
                for (i in 0..xAxisLabels.size - 1) {
                    val x = i * widthPerDay + xOffset
                    drawContext.canvas.nativeCanvas.drawText(
                        xAxisLabels[i],
                        x,
                        size.height + 45f,
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 28f
                        }
                    )
                }

                // Create path for more wavy line
                val path = Path().apply {
                    moveTo(
                        xOffset,
                        size.height * (1 - (hoursData[0] - minValue) / (maxValue - minValue)) - 25f
                    )

                    for (i in 1 until hoursData.size) {
                        val prevX = (i - 1) * widthPerDay + xOffset
                        val prevY =
                            size.height * (1 - (hoursData[i - 1] - minValue) / (maxValue - minValue)) - 25f

                        val currX = i * widthPerDay + xOffset
                        val currY =
                            size.height * (1 - (hoursData[i] - minValue) / (maxValue - minValue)) - 25f

                        // Adjust control points for stronger wave effect
                        val controlX1 = prevX + widthPerDay / 2 * waveAmplitude
                        val controlY1 = prevY

                        val controlX2 = currX - widthPerDay / 2 * waveAmplitude
                        val controlY2 = currY

                        // Draw cubic Bézier curve with amplified wave effect
                        cubicTo(controlX1, controlY1, controlX2, controlY2, currX, currY)
                    }
                }

                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(size.width, size.height)
                    lineTo(xOffset, size.height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent),
                        startY = 0f,
                        endY = size.height
                    )
                )

                // Draw the more wavy line
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Draw Y-axis line
                drawLine(
                    color = Color(0xffA1A1A1),
                    start = yAxisStart,
                    end = yAxisEnd,
                    strokeWidth = 3f
                )

                // Draw X-axis line
                drawLine(
                    color = Color(0xffA1A1A1),
                    start = xAxisStart,
                    end = xAxisEnd,
                    strokeWidth = 3f
                )
            }
        }
    }
}

fun calculateTimeDifference(maxHour: Int): Int {
    var start = 5
    while (true) {
        val div = (maxHour.toFloat() / start.toFloat())
        if (div <= 4.0) {
            break
        } else {
            start += 5
        }
    }
    return start
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffFFFDFC)
@Composable
fun PreviewWaveLineChartWithAxes() {
    val hoursWorked = listOf(10f, 12f, 20f, 5f, 0f, 4f, 17f)

    WaveLineChartWithAxes(
        hoursData = hoursWorked,
        maxHour = 30,
        timeRange = TimeRange.Week,
        daysInMonth = 30,
        month = 9,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp),
        lineColor = Color(0xff9463ED),
        strokeWidth = 9f,
        xOffset = 90f,
        waveAmplitude = 1f
    )
}

enum class TimeRange {
    Today, Week, Month, Year
}
