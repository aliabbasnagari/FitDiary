package com.cloudcare.fitdiary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.repository.AuthRepository
import java.time.LocalTime


@Composable
fun SettingsScreen(authRepository: AuthRepository) {
    var reminderTime by remember { mutableStateOf(LocalTime.of(20, 0)) }
    var isDarkTheme by remember { mutableStateOf(false) }

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
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { isDarkTheme = it },
            modifier = Modifier.padding(16.dp)
        )
        Text("Dark Theme: ${if (isDarkTheme) "On" else "Off"}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authRepository.logout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}