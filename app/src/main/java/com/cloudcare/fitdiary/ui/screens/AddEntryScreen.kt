package com.cloudcare.fitdiary.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.repository.HealthRepository
import com.cloudcare.fitdiary.data.repository.MockHealthRepository
import com.cloudcare.fitdiary.ui.theme.FitDiaryTheme
import java.time.LocalDate

@Composable
fun AddEntryScreen(healthRepository: HealthRepository) {
    val today = LocalDate.now().toString()
    var waterIntake by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("😊") }
    var weight by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    fun loadTodayData() {
        healthRepository.getHealthEntries(today, today, { entries ->
            val entry = entries.firstOrNull()
            if (entry != null) {
                waterIntake = entry.waterIntake.toString()
                sleepHours = entry.sleepHours.toString()
                steps = entry.steps.toString()
                mood = entry.mood
                weight = entry.weight.toString()
            }

            isLoading = false
        }, {
            isLoading = false
        })
    }

    LaunchedEffect(Unit) {
        loadTodayData()
    }

    // Validation function
    fun validateInputs(): Boolean {
        return when {
            waterIntake.isEmpty() || waterIntake.toFloatOrNull() == null || waterIntake.toFloat() <= 0 -> {
                errorMessage = "Please enter a valid water intake (ml)"
                false
            }

            sleepHours.isEmpty() || sleepHours.toFloatOrNull() == null || sleepHours.toFloat() <= 0 -> {
                errorMessage = "Please enter valid sleep hours"
                false
            }

            steps.isEmpty() || steps.toIntOrNull() == null || steps.toInt() <= 0 -> {
                errorMessage = "Please enter valid step count"
                false
            }

            weight.isEmpty() || weight.toFloatOrNull() == null || weight.toFloat() <= 0 -> {
                errorMessage = "Please enter a valid weight (kg)"
                false
            }

            else -> {
                errorMessage = ""
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Add Health Entry",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Input Fields with Numeric Keyboard
        OutlinedTextField(
            value = waterIntake,
            onValueChange = { waterIntake = it },
            label = { Text("Water Intake (ml)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = waterIntake.isNotEmpty() && (waterIntake.toFloatOrNull() == null || waterIntake.toFloat() <= 0)
        )

        OutlinedTextField(
            value = sleepHours,
            onValueChange = { sleepHours = it },
            label = { Text("Sleep Hours") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = sleepHours.isNotEmpty() && (sleepHours.toFloatOrNull() == null || sleepHours.toFloat() <= 0)
        )

        OutlinedTextField(
            value = steps,
            onValueChange = { steps = it },
            label = { Text("Steps") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = steps.isNotEmpty() && (steps.toIntOrNull() == null || steps.toInt() <= 0)
        )

        // Mood Selection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                "Select Mood",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("😊" to "Happy", "😐" to "Neutral", "😔" to "Sad").forEach { (emoji, label) ->
                    Button(
                        onClick = { mood = emoji },
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mood == emoji) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            contentColor = if (mood == emoji) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (mood == emoji) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = emoji,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = weight.isNotEmpty() && (weight.toFloatOrNull() == null || weight.toFloat() <= 0)
        )

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Save Button
        Button(
            onClick = {
                if (validateInputs()) {
                    isSaving = true
                    val entry = HealthEntry(
                        date = LocalDate.now().toString(),
                        waterIntake = waterIntake.toFloat(),
                        sleepHours = sleepHours.toFloat(),
                        steps = steps.toInt(),
                        mood = mood,
                        weight = weight.toFloat()
                    )
                    healthRepository.saveHealthEntry(
                        entry,
                        onSuccess = {
                            isSaving = false
                            errorMessage = "Entry saved successfully!"
                        },
                        onFailure = {
                            isSaving = false
                            errorMessage = "Failed to save entry. Please try again."
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            enabled = !isSaving
        ) {
            Text(if (isSaving) "Saving..." else "Save Entry")
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