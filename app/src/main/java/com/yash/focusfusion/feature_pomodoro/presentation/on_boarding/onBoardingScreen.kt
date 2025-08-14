package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.AnimatedPreloaderLottie
import com.yash.focusfusion.ui.theme.fontFamily

@Composable
fun onBoardingScreen(
    onNavigateToMain: () -> Unit,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel<OnBoardingViewModel>(),
    modifier: Modifier = Modifier,
) {

    val uiState by onBoardingViewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            OnboardingNavigationEvent.NavigateToMain -> {
                onNavigateToMain()
                onBoardingViewModel.onEvent(event = OnBoardingUiEvent.OnNavigationEventConsumed)
            }

            null -> Unit
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box {
            AnimatedPreloaderLottie(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = "focus fusion",
            fontSize = 42.sp,
            color = Color(0xffFF8D61),
            fontFamily = FontFamily(Font(R.font.fugaz_one_regular))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Take control of your time with focused work intervals.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {

            // Name Input Section
            Text(
                text = "What should we call you?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.userName,
                onValueChange = {
                    onBoardingViewModel.onEvent(
                        OnBoardingUiEvent.OnNameChanged(it)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("Enter your name") },
                singleLine = true,
                isError = !uiState.isNameValid,
                supportingText = if (!uiState.isNameValid || uiState.error != null) {
                    {
                        Text(
                            text = uiState.error ?: "Name must be at least 2 characters long",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else null,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xff8958E2),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Get Started Button
            Button(
                onClick = {
                    keyboardController?.hide()
                    onBoardingViewModel.onEvent(
                        OnBoardingUiEvent.OnGetStartedClicked
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading && uiState.userName.trim().isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffFF8D61)
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Let's Go",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnBoardingScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box {
            AnimatedPreloaderLottie(
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = "focus fusion",
            fontSize = 42.sp,
            color = Color(0xffFF8D61),
            fontFamily = FontFamily(Font(R.font.fugaz_one_regular))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Take control of your time with focused work intervals.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {

            // Name Input Section
            Text(
                text = "What should we call you?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter your name") },
                singleLine = true,
                isError = false,
                supportingText = if (false) {
                    {
                        Text(
                            text = "error" ?: "Name must be at least 2 characters long",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else null,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xff8958E2),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Get Started Button
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xff8958E2)
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                if (false) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Get Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }


}