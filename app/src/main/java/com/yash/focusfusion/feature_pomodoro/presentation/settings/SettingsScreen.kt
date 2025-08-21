package com.yash.focusfusion.feature_pomodoro.presentation.settings

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yash.focusfusion.R
import com.yash.focusfusion.core.annotations.DevicePreviews
import com.yash.focusfusion.core.util.loadHtmlFromRaw
import com.yash.focusfusion.feature_pomodoro.presentation.settings.components.ThemePreferenceItem
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import com.yash.focusfusion.ui.theme.ThemeMode
import kotlin.math.sin

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    modifier: Modifier = Modifier,
) {

    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    var inputName by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val htmlContent = remember {
        loadHtmlFromRaw(context, R.raw.privacy_policy)
    }
    val packageName = context.packageName


    val cardBackgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val headerColor = MaterialTheme.colorScheme.onBackground
    val iconColor = MaterialTheme.colorScheme.onSurface
    
    val theme by settingsViewModel.themeState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            Image(
                painter = painterResource(R.drawable.profile_char),
                contentDescription = "Profile Char",
                modifier = Modifier
                    .size(100.dp)
                    .zIndex(1f)
                    .offset(y = -20.dp)

            )

            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .background(cardBackgroundColor, shape = RoundedCornerShape(20.dp))

            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = uiState.name,
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                        color = textColor
                    )

                    IconButton(onClick = {
                        settingsViewModel.onEvent(SettingsUiEvent.ShowNameChangeDialog)
                    }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit Name",
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

            }
        }

        Spacer(Modifier.height(20.dp))
        Text(
            text = "Appearance & Sound",
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))
        ThemePreferenceItem(
            selectedTheme = theme,
            onThemeSelected = { newTheme ->
                settingsViewModel.onEvent(SettingsUiEvent.OnThemeChanged(newTheme))
            }
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Timer Settings",
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            fontSize = 20.sp,
            color = headerColor
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(20.dp))
                .background(cardBackgroundColor, shape = RoundedCornerShape(20.dp))

        ) {

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Timer Interval",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium)),
                    color = textColor
                )

                var expanded by remember { mutableStateOf(false) }
                val items = (5..50 step 5).toList()
                Log.d("FOCUSTIMEINTERVAL", "Ui State:- ${uiState.timeInterval}")
                val poistionOfSelectedItem =
                    items.indexOf(uiState.timeInterval)

                var selectedItem by remember(poistionOfSelectedItem) {
                    mutableStateOf(items[poistionOfSelectedItem])
                }

                Box(modifier = Modifier.padding(1.dp)) {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(
                            selectedItem.toString() + "min",
                            fontFamily = FontFamily(Font(R.font.jost_medium)),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "",
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        properties = PopupProperties(focusable = true)

                    ) {
                        Column {
                            items.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(label.toString()) },
                                    onClick = {
                                        selectedItem = label
                                        expanded = false
                                        settingsViewModel.onEvent(
                                            event = SettingsUiEvent.onTimerChange(
                                                label
                                            )
                                        )
                                    }
                                )
                            }
                        }

                    }
                }
            }

        }


        Spacer(Modifier.height(20.dp))

        Text(
            text = "Contact & Policy",
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            fontSize = 20.sp,
            color = headerColor
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(1.dp, RoundedCornerShape(20.dp))
                .background(cardBackgroundColor, shape = RoundedCornerShape(20.dp))
                .clickable {
                    settingsViewModel.onEvent(SettingsUiEvent.ShowPrivacyPolicyDialog)
                },
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Privacy Policy",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                color = textColor
            )

        }

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(20.dp))
                .background(cardBackgroundColor, shape = RoundedCornerShape(20.dp))
                .clickable {
                    try {
                        // Try to open the Play Store app directly
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        // If the Play Store is not available, open in a web browser
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                        context.startActivity(intent)
                    }
                }
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rate Us",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium)),
                    color = textColor
                    )

                Row {
                    (1..5).forEach {
                        Image(
                            painter = painterResource(R.drawable.baseline_star_24),
                            contentDescription = "star",
                        )
                    }
                }
            }

        }
    }



    if (uiState.isNameDialogvisible) {
        AlertDialog(
            onDismissRequest = {
                settingsViewModel.onEvent(SettingsUiEvent.HideNameChangeDialog)
            },
            title = {
                Text(text = "Change Name")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("Name") },
                        isError = uiState.error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    settingsViewModel.onEvent(
                        SettingsUiEvent.onNameChanged(
                            inputName
                        )
                    )
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { settingsViewModel.onEvent(SettingsUiEvent.HideNameChangeDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }


    if (uiState.isPrivacyPolicyDialogVisible) {
        val dialogTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
        AlertDialog(
            onDismissRequest = {
                settingsViewModel.onEvent(SettingsUiEvent.HidePrivacyPolicyDialog)
            },
            title = {
                Text(text = "Privacy Policy")
            },
            text = {
                Column {
                    LazyColumn {
                        item {
                            AndroidView(factory = {
                                TextView(it).apply {
                                    text = htmlContent
                                    textSize = 16f
                                    setTextColor(dialogTextColor)
                                    setPadding(24, 24, 24, 24)
                                }
                            })
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    settingsViewModel.onEvent(
                        SettingsUiEvent.HidePrivacyPolicyDialog
                    )
                }) {
                    Text("Ok")
                }
            },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    FocusFusionTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Image(
                    painter = painterResource(R.drawable.profile_char),
                    contentDescription = "Profile Char",
                    modifier = Modifier
                        .size(120.dp)
                        .zIndex(1f)
                        .offset(y = 0.dp)

                )

                Column(
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .shadow(1.dp, RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20.dp))

                ) {

                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Yashveer",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.jost_medium)),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Edit Name",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Appearance & Sound",
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(12.dp))
            ThemePreferenceItem(
                selectedTheme = ThemeMode.SYSTEM,
                onThemeSelected = { newTheme ->

                }
            )


            Spacer(Modifier.height(20.dp))

            Text(
                text = "Timer Settings",
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp))

            ) {

                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Timer Interval",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    var expanded by remember { mutableStateOf(false) }
                    val items = listOf(15, 25, 30, 45, 50)
                    var selectedItem by remember { mutableStateOf(items[0]) }

                    Box(modifier = Modifier.padding(5.dp)) {
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(
                                selectedItem.toString() + "min",
                                fontFamily = FontFamily(Font(R.font.jost_medium)),
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end = 15.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = ""
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                            properties = PopupProperties(focusable = true)

                        ) {
                            Column {
                                items.forEach { label ->
                                    DropdownMenuItem(
                                        text = { Text(label.toString()) },
                                        onClick = {
                                            selectedItem = label
                                            expanded = false
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Contact & Policy",
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                    .clickable {

                    }

            ) {
                Text(
                    text = "Privacy Policy",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium)),
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                    .clickable {
                    }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Rate Us",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                        color = MaterialTheme.colorScheme.onSurface
                        )

                    Row {
                        (1..5).forEach {
                            Image(
                                painter = painterResource(R.drawable.baseline_star_24),
                                contentDescription = "star",
                            )
                        }
                    }
                }

            }

        }
    }
}