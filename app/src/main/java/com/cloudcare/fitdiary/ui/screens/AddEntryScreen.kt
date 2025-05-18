package com.cloudcare.fitdiary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.repository.HealthRepository
import com.cloudcare.fitdiary.data.repository.MockHealthRepository
import com.cloudcare.fitdiary.ui.theme.FitDiaryTheme
import java.time.LocalDate

@Composable
fun AddEntryScreen(healthRepository: HealthRepository) {
    var waterIntake by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("😊") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Add Health Entry", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(
                value = waterIntake,
                onValueChange = { waterIntake = it },
                label = { Text("Water Intake (ml)") }
            )
            OutlinedTextField(
                value = sleepHours,
                onValueChange = { sleepHours = it },
                label = { Text("Sleep Hours") }
            )
            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("Steps") }
            )
            Row {
                listOf("😊", "😐", "😔").forEach { emoji ->
                    Button(onClick = { mood = emoji }) {
                        Text(emoji)
                    }
                }
            }
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") }
            )
            Button(onClick = {
                val entry = HealthEntry(
                    date = LocalDate.now().toString(),
                    waterIntake = waterIntake.toFloatOrNull() ?: 0f,
                    sleepHours = sleepHours.toFloatOrNull() ?: 0f,
                    steps = steps.toIntOrNull() ?: 0,
                    mood = mood,
                    weight = weight.toFloatOrNull() ?: 0f
                )
                healthRepository.saveHealthEntry(
                    entry,
                    { /*Handle Success */ },
                    { /* Handle error */ }
                )
            }) {
                Text("Save Entry")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddEntryScreenPreview() {
    FitDiaryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddEntryScreen(
                healthRepository = MockHealthRepository()
            )
        }
    }
}