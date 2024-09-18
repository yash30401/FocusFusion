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
@Composable
fun WaveLineChartWithAxes(
    minutesData: List<Float>,
    timeRange: TimeRange,
    daysInMonth: Int,
    month: Int,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF9463ED),
    strokeWidth: Float = 4f,
    xOffset: Float = 50f,  // Shift the X-axis and wave to the right
    waveAmplitude: Float = 2f  // Amplify the wave effect
) {
    val minValue = 0f   // Y-axis min (0 minutes)
    val maxValue = (minutesData.maxOrNull() ?: minValue).coerceAtLeast(minValue)  // Y-axis max based on data

    // Decide on number of Y-axis steps (e.g., 4 steps)
    val numberOfSteps = 4
    val yAxisStep = (maxValue - minValue) / numberOfSteps.toFloat()

    Box(
        modifier = modifier
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header UI code here (omitted for brevity)

            // Canvas for the chart
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)  // Adjust height for better fit
                    .padding(top = 20.dp)
            ) {
                // X-axis labels for different time ranges
                val xAxisLabels = when (timeRange) {
                    TimeRange.Today -> listOf("00:00",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "06:00",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "12:00",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "18:00",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    )
                    TimeRange.Week -> listOf("M", "T", "W", "T", "F", "S", "S")
                    TimeRange.Month -> {
                        if (daysInMonth == 30) {
                            listOf("${month}/1", "${month}/8", "${month}/16", "${month}/23", "${month}/30")
                        } else {
                            listOf("${month}/1", "${month}/9", "${month}/16", "${month}/24", "${month}/31")
                        }
                    }
                    TimeRange.Year -> listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                }

                val leftPadding = xOffset
                val rightPadding = xOffset
                val widthPerLabel = (size.width - leftPadding - rightPadding) / (xAxisLabels.size - 1)
                val heightPerUnit = size.height / (maxValue - minValue)

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
                        String.format("%.0f min", yLabelValue),
                        leftPadding - 10f,
                        y + 10f,  // Adjust for text height
                        textPaint
                    )
                }

                // X-axis labels
                for (i in xAxisLabels.indices) {
                    val x = leftPadding + i * widthPerLabel
                    drawContext.canvas.nativeCanvas.drawText(
                        xAxisLabels[i],
                        x,
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

                val fillPath = Path().apply {
                    addPath(wavePath)
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

                // Draw the wave line
                drawPath(
                    path = wavePath,
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
                    end = Offset(size.width - rightPadding, size.height),
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
    val minutesWorked = listOf(
        60f, 120f, 0f, 0f, 0f, 120f, 400f, 0f,
        0f, 0f, 300f, 0f, 0f, 0f, 0f, 0f, 240f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
    )

    WaveLineChartWithAxes(
        minutesData = minutesWorked,
        timeRange = TimeRange.Today,
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
