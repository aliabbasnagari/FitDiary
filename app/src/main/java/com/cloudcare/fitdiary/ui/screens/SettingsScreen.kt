package com.cloudcare.fitdiary.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HorizontalDistribute
import androidx.compose.material.icons.filled.VerticalDistribute
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloudcare.fitdiary.data.model.ChartMode
import com.cloudcare.fitdiary.data.model.SettingsDataStore
import com.cloudcare.fitdiary.data.model.ThemeMode
import com.cloudcare.fitdiary.data.repository.AuthRepository
import kotlinx.coroutines.launch
import java.time.LocalTime


@Composable
fun SettingsScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    val context = LocalContext.current
    val settings = SettingsDataStore(context)
    val coroutineScope = rememberCoroutineScope()

    val themeMode by settings.getThemeMode.collectAsState(initial = ThemeMode.SYSTEM_DEFAULT)
    var selectedThemeMode by remember { mutableStateOf(themeMode) }
    LaunchedEffect(themeMode) {
        Log.d("SETTING", themeMode.name)
        if (selectedThemeMode != themeMode) {
            selectedThemeMode = themeMode
        }
    }
    LaunchedEffect(selectedThemeMode) {
        Log.d("SETTING", selectedThemeMode.name)
        if (selectedThemeMode != themeMode) {
            coroutineScope.launch { settings.setThemeMode(selectedThemeMode) }
        }
    }

    val chartMode by settings.getChartMode.collectAsState(initial = ChartMode.HORIZONTAL)
    var selectedChartMode by remember { mutableStateOf(chartMode) }
    LaunchedEffect(chartMode) {
        Log.d("SETTING", chartMode.name)
        if (selectedChartMode != chartMode) {
            selectedChartMode = chartMode
        }
    }
    LaunchedEffect(selectedChartMode) {
        Log.d("SETTING", chartMode.name)
        if (selectedChartMode != chartMode) {
            coroutineScope.launch { settings.setChartMode(selectedChartMode) }
        }
    }

    var reminderTime by remember { mutableStateOf(LocalTime.of(20, 0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Text("Reminder Time")
        Button(onClick = { reminderTime = reminderTime.plusHours(1) }) {
            Text("Set to ${reminderTime.plusHours(1)}")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .selectableGroup()
        ) {
            Text(
                text = "App Theme",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            ThemeMode.entries.forEach { mode ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (mode == selectedThemeMode),
                            onClick = { selectedThemeMode = mode },
                            role = Role.RadioButton
                        )
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (mode == selectedThemeMode),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = mode.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Charts Layout",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Vertical",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedChartMode != ChartMode.HORIZONTAL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = selectedChartMode == ChartMode.HORIZONTAL,
                    onCheckedChange = {
                        selectedChartMode = if (it) ChartMode.HORIZONTAL else ChartMode.VERTICAL
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    thumbContent = if (selectedChartMode == ChartMode.HORIZONTAL) {
                        {
                            Icon(
                                imageVector = Icons.Filled.VerticalDistribute,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        {
                            Icon(
                                imageVector = Icons.Filled.HorizontalDistribute,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                )
                Text(
                    text = "Horizontal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedChartMode == ChartMode.HORIZONTAL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authRepository.logout()
                navController.navigate("login")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,       // red background
                contentColor = MaterialTheme.colorScheme.onError        // contrasting text color
            )
        ) {
            Text("Logout")
        }
    }
}