package com.cloudcare.fitdiary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.repository.HealthRepository
import java.time.LocalDate


@Composable
fun DashboardScreen(healthRepository: HealthRepository) {
    val today = LocalDate.now().toString()
    var entry by remember { mutableStateOf<HealthEntry?>(null) }

    LaunchedEffect(Unit) {
        healthRepository.getHealthEntries(today, today, { entries ->
            entry = entries.firstOrNull()
        }, { /* Handle error */ })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Today's Health Summary", style = MaterialTheme.typography.headlineMedium)
        entry?.let {
            Text("Water: ${it.waterIntake} ml")
            Text("Sleep: ${it.sleepHours} hours")
            Text("Steps: ${it.steps}")
            Text("Mood: ${it.mood}")
            Text("Weight: ${it.weight} kg")
        } ?: Text("No data for today")

        // Placeholder for MPAndroidChart (requires View-based integration)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Trend Chart Placeholder")
    }
}
