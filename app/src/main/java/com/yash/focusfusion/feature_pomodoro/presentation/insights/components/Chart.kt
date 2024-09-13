package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yash.focusfusion.R

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
@Composable
fun WaveLineChartWithAxes(
    days: List<String>,
    hoursData: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF9463ED),
    strokeWidth: Float = 4f,
    xOffset: Float = 50f,  // Shift the X-axis and wave to the right
    waveAmplitude: Float = 2f  // Amplify the wave effect
) {
    val maxValue = 12f  // Y-axis max (12 hours)
    val minValue = 0f   // Y-axis min (0 hours)
    val yAxisStep = 3f  // Y-axis step (3-hour intervals)

    Box(modifier = modifier
        .shadow(2.dp, shape = RoundedCornerShape(20.dp))
        .background(Color(0xffF8F8F8), RoundedCornerShape(20.dp))
        .padding(30.dp)
        ,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(300.dp)) {
            val widthPerDay = (size.width - xOffset) / (hoursData.size - 1)
            val yAxisGap = size.height / ((maxValue - minValue) / yAxisStep)

            // Calculate positions for the axes
            val xAxisStart = Offset(xOffset - 20f, size.height - 10f)
            val yAxisStart = Offset(xOffset - 20f, 0f - 25f)
            val xAxisEnd = Offset(size.width + 20f, size.height - 10f)
            val yAxisEnd = Offset(xOffset - 20f, size.height - 10f)

            // Draw Y-axis grid lines and labels (0h, 3h, 6h, 9h, 12h)
            for (i in 0..4) {
                val y = size.height - (i * yAxisGap)
                drawContext.canvas.nativeCanvas.drawText(
                    "${i * 3}h",
                    10f,
                    y - 10f,
                    Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 28f
                    }
                )
            }

            // Draw X-axis labels (Days of the week) with right shift (xOffset)
            for (i in hoursData.indices) {
                val x = i * widthPerDay + xOffset
                drawContext.canvas.nativeCanvas.drawText(
                    days[i],
                    x,
                    size.height + 30f,
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
                    size.height * (1 - (hoursData[0] - minValue) / (maxValue - minValue))
                )

                for (i in 1 until hoursData.size) {
                    val prevX = (i - 1) * widthPerDay + xOffset
                    val prevY =
                        size.height * (1 - (hoursData[i - 1] - minValue) / (maxValue - minValue))

                    val currX = i * widthPerDay + xOffset
                    val currY =
                        size.height * (1 - (hoursData[i] - minValue) / (maxValue - minValue))

                    // Adjust control points for stronger wave effect
                    val controlX1 = prevX + widthPerDay / 2 * waveAmplitude
                    val controlY1 = prevY

                    val controlX2 = currX - widthPerDay / 2 * waveAmplitude
                    val controlY2 = currY

                    // Draw cubic Bézier curve with amplified wave effect
                    cubicTo(controlX1, controlY1, controlX2, controlY2, currX, currY)
                }
            }

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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffFFFDFC)
@Composable
fun PreviewWaveLineChartWithAxes() {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
    val hoursWorked = listOf(10f, 2f, 4f, 9f, 6.5f, 10f, 1f)

    WaveLineChartWithAxes(
        days = daysOfWeek,
        hoursData = hoursWorked,
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
