package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalDial(
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit,
) {

}

@Preview(
    showBackground = true, showSystemUi = true,
    backgroundColor = 0xffFFFFFF
)
@Composable
private fun HorizontalDialPrev() {
        val numbers = (0..50 step 5)

}