package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HorizontalDial(
    modifier: Modifier = Modifier,
    steps: List<Int> = (5..50 step 5).toList(),
    initialValue: Int = 25,
    onValueChange: (Int) -> Unit,
) {
    val itemWidth = 120.dp
    val selectedTextStyle = TextStyle(
        color = Color.Black,
        fontSize = 42.sp,
        fontFamily = FontFamily(Font(R.font.fugaz_one_regular))
    )
    val fadedTextStyle = TextStyle(
        color = Color.Gray,
        fontSize = 38.sp,
        fontWeight = FontWeight.Normal,
    )
    val hapticFeedback = LocalHapticFeedback.current

    // --- State for the LazyRow and snapping behavior ---
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = steps.indexOf(initialValue).coerceAtLeast(0)
    )
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // --- Derived state to find the currently centered item ---
    val selectedIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) {
                -1
            } else {
                val viewportCenter =
                    layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2
                visibleItems.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }?.index
                    ?: -1
            }
        }
    }

    // --- Effect to notify parent of value change when scrolling stops ---
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && selectedIndex != -1) {
            onValueChange(steps[selectedIndex])
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val halfWidth = maxWidth / 2

        LazyRow(
            state = listState,
            flingBehavior = snapBehavior,
            modifier = Modifier.fillMaxWidth(),
            // Add padding to allow first and last items to be centered
            contentPadding = PaddingValues(horizontal = halfWidth - itemWidth / 2),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(steps.size) { index ->
                val isSelected = (index == selectedIndex)
                val style = if (isSelected) selectedTextStyle else fadedTextStyle
                val alpha = if (isSelected) 1f else 0.3f

                Box(
                    modifier = Modifier.width(itemWidth),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = steps[index].toString(),
                            modifier = Modifier.alpha(alpha),
                            style = style,
                            textAlign = TextAlign.Center
                        )

                        if (isSelected) {
                            Box(
                                modifier =
                                    Modifier
                                        .width(30.dp)
                                        .height(5.dp)
                                        .background(Color(0xffFF8D61))
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(
    showBackground = true, showSystemUi = true,
    backgroundColor = 0xffFFFFFF
)
@Composable
private fun HorizontalDialPrev() {
        
    HorizontalDial {  }
}