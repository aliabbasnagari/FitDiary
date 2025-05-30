package com.cloudcare.fitdiary.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.repository.HealthRepository
import com.cloudcare.fitdiary.data.repository.MockHealthRepository
import com.cloudcare.fitdiary.ui.components.HealthListItem
import com.cloudcare.fitdiary.ui.components.MyDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@Composable
fun HistoryScreen(healthRepository: HealthRepository) {
    val context = LocalContext.current
    var entries by remember { mutableStateOf<List<HealthEntry>>(emptyList()) }
    var startDate by remember { mutableStateOf(LocalDate.now().minusDays(30)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(startDate, endDate) {
        healthRepository.getHealthEntries(
            startDate.toString(),
            endDate.toString(),
            {
                entries = it
                isLoading = false
            },
            { isLoading = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Health History", style = MaterialTheme.typography.headlineMedium)
        Row {
            MyDatePicker(title = "From", initialDate = startDate, onDateSelected = { date ->
                startDate = date?.let {
                    Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
            })

            MyDatePicker(
                title = "To",
                initialDate = endDate,
                lowerBound = startDate,
                onDateSelected = { date ->
                    endDate = date?.let {
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                })

        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(entries) { entry ->
                HealthListItem(entry)
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        Button(onClick = { exportToCSV(entries, context) }) {
            Text("Export to CSV")
        }
    }
}

fun exportToCSV(entries: List<HealthEntry>, context: Context) {
    val csvContent = StringBuilder()
    csvContent.append("\uFEFF")
    csvContent.append("Date,WaterIntake,SleepHours,Steps,Mood,Weight\n")
    entries.forEach { entry ->
        csvContent.append("${entry.date},${entry.waterIntake},${entry.sleepHours},${entry.steps},${entry.mood},${entry.weight}\n")
    }
    // Save to file (requires storage permissions)
    saveFileToDownloads(context, "health_data.csv", csvContent.toString())
}

fun saveFileToDownloads(context: Context, fileName: String, content: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    outputStream?.write(content.toByteArray())
                    Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, "Failed to create file", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(healthRepository = MockHealthRepository())
}