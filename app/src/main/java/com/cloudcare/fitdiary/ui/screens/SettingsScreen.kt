package com.cloudcare.fitdiary.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HorizontalDistribute
import androidx.compose.material.icons.filled.VerticalDistribute
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.produceState
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
import com.cloudcare.fitdiary.data.model.ChartSpan
import com.cloudcare.fitdiary.data.model.SettingsDataStore
import com.cloudcare.fitdiary.data.model.ThemeMode
import com.cloudcare.fitdiary.data.repository.AuthRepository
import com.cloudcare.fitdiary.ui.components.MyTimePicker
import com.cloudcare.fitdiary.worker.WorkScheduler
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun SettingsScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    val context = LocalContext.current
    val settings = SettingsDataStore(context)
    val coroutineScope = rememberCoroutineScope()

    val reminderTime by produceState(
        initialValue = LocalTime.of(20, 0)
    ) { settings.getReminderTime.collect { value = it } }
    var showTimePicker by remember { mutableStateOf(false) }

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

    val chartSpan by settings.getChartSpan.collectAsState(initial = ChartSpan.MONTH)
    var selectedChartSpan by remember { mutableStateOf(chartSpan) }
    LaunchedEffect(chartSpan) {
        Log.d("SETTING", chartSpan.name)
        if (selectedChartSpan != chartSpan) {
            selectedChartSpan = chartSpan
        }
    }
    LaunchedEffect(selectedChartSpan) {
        Log.d("SETTING", selectedChartSpan.name)
        if (selectedChartSpan != chartSpan) {
            coroutineScope.launch { settings.setChartSpan(selectedChartSpan) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

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
                text = "Daily Remainder",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                )
                Text(
                    text = reminderTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    showTimePicker = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Favorite"
                    )
                }
            }

            if (showTimePicker) {
                MyTimePicker(
                    initialTime = reminderTime,
                    onTimeSelected = { hour, minute ->
                        val newTime = LocalTime.of(hour, minute)
                        WorkScheduler.scheduleDailyReminder(context, newTime)
                        coroutineScope.launch {
                            settings.setReminderTime(newTime)
                            WorkScheduler.scheduleDailyReminder(context, newTime)
                            Toast.makeText(
                                context,
                                "Reminder set for ${newTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        showTimePicker = false
                    },
                    onDismiss = { showTimePicker = false }
                )
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
                .selectableGroup()
        ) {
            Text(
                text = "App Theme",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(
                modifier = Modifier.height(10.dp)
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
                text = "Charts Theme",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = "Chart Layout",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
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
            Text(
                text = "Chart Span",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Week Badge
                Badge(
                    modifier = Modifier
                        .clickable { selectedChartSpan = ChartSpan.WEEK },
                    containerColor = if (selectedChartSpan == ChartSpan.WEEK) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (selectedChartSpan == ChartSpan.WEEK) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                ) {
                    Text(
                        text = "Week",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Month Badge
                Badge(
                    modifier = Modifier
                        .clickable { selectedChartSpan = ChartSpan.MONTH },
                    containerColor = if (selectedChartSpan == ChartSpan.MONTH) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (selectedChartSpan == ChartSpan.MONTH) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                ) {
                    Text(
                        text = "Month",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authRepository.logout()
                WorkScheduler.cancelReminders(context)
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