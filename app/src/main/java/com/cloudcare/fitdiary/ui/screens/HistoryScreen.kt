package com.cloudcare.fitdiary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.repository.HealthRepository
import java.time.LocalDate


@Composable
fun HistoryScreen(healthRepository: HealthRepository) {
    var entries by remember { mutableStateOf<List<HealthEntry>>(emptyList()) }
    var startDate by remember { mutableStateOf(LocalDate.now().minusDays(30)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(startDate, endDate) {
        healthRepository.getHealthEntries(
            startDate.toString(),
            endDate.toString(),
            { entries = it },
            { /* Handle error */ }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Health History", style = MaterialTheme.typography.headlineMedium)
        Row {
            Button(onClick = { startDate = startDate.minusDays(7) }) {
                Text("Previous Week")
            }
            Button(onClick = { startDate = startDate.plusDays(7) }) {
                Text("Next Week")
            }
        }
        LazyColumn {
            items(entries) { entry ->
                Text("${entry.date}: Water=${entry.waterIntake}ml, Steps=${entry.steps}")
            }
        }
        Button(onClick = { exportToCSV(entries) }) {
            Text("Export to CSV")
        }
    }
}

fun exportToCSV(entries: List<HealthEntry>) {
    val csvContent = StringBuilder()
    csvContent.append("Date,WaterIntake,SleepHours,Steps,Mood,Weight\n")
    entries.forEach { entry ->
        csvContent.append("${entry.date},${entry.waterIntake},${entry.sleepHours},${entry.steps},${entry.mood},${entry.weight}\n")
    }
    // Save to file (requires storage permissions)
}