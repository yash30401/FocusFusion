package com.yash.focusfusion.feature_pomodoro.presentation.settings.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import com.yash.focusfusion.ui.theme.ThemeMode

@Composable
fun ThemePreferenceItem(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val themeName = selectedTheme.name.lowercase().replaceFirstChar { it.uppercase() }

    var isSoundEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Theme",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                color = MaterialTheme.colorScheme.onSurface
            )
            Box {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        themeName,
                        fontFamily = FontFamily(Font(R.font.jost_medium)),
                        fontSize = 12.sp
                    )
                    Icon(
                        Icons.Filled.ArrowDropDown, contentDescription = "Select Theme"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    ThemeMode.values().forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    theme.name.lowercase().replaceFirstChar { it.uppercase() })
                            },
                            onClick = {
                                onThemeSelected(theme)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Play Sound When Session Ends",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.jost_medium)),
                color = MaterialTheme.colorScheme.onSurface
            )

            Switch(checked = isSoundEnabled, onCheckedChange = {
                isSoundEnabled = !isSoundEnabled
            })

        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ThemeSoundPrefItemPreview() {

    FocusFusionTheme {
        ThemePreferenceItem(ThemeMode.SYSTEM) {

        }
    }

}