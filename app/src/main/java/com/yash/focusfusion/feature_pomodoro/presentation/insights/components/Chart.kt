package com.yash.focusfusion.feature_pomodoro.presentation.insights.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WaveLineChartWithAxes(
    days: List<String>,
    hoursData: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue,
    strokeWidth: Float = 4f
) {
    val maxValue = 12f  // Y-axis max (12 hours)
    val minValue = 0f   // Y-axis min (0 hours)
    val yAxisStep = 3f  // Y-axis step (3-hour intervals)

    Canvas(modifier = modifier) {
        val widthPerDay = size.width / (hoursData.size - 1)
        val yAxisGap = size.height / ((maxValue - minValue) / yAxisStep)

        // Draw Y-axis grid lines and labels (0h, 3h, 6h, 9h, 12h)
        for (i in 0..4) {
            val y = size.height - (i * yAxisGap)
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${i * 3}h",
                10f,
                y + 10f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 28f
                }
            )
        }

        // Draw X-axis labels (Days of the week)
        for (i in hoursData.indices) {
            val x = i * widthPerDay
            drawContext.canvas.nativeCanvas.drawText(
                days[i],
                x,
                size.height + 30f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 28f
                }
            )
        }

        // Create path for wave-like line
        val path = Path().apply {
            moveTo(0f, size.height * (1 - (hoursData[0] - minValue) / (maxValue - minValue)))

            for (i in 1 until hoursData.size) {
                val x1 = (i - 1) * widthPerDay + widthPerDay / 2
                val y1 = size.height * (1 - (hoursData[i - 1] - minValue) / (maxValue - minValue))

                val x2 = i * widthPerDay
                val y2 = size.height * (1 - (hoursData[i] - minValue) / (maxValue - minValue))

                // Draw quadratic Bézier curve for wave effect
                quadraticBezierTo(x1, y1, x2, y2)
            }
        }

        // Draw the wave-like line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Preview
@Composable
fun PreviewWaveLineChartWithAxes() {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
    val hoursWorked = listOf(10f, 7f, 4f, 9f, 6.5f, 10f, 1f)

    WaveLineChartWithAxes(
        days = daysOfWeek,
        hoursData = hoursWorked,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp),
        lineColor = Color.Magenta,
        strokeWidth = 5f
    )
}
