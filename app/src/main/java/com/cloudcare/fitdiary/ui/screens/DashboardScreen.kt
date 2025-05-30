package com.cloudcare.fitdiary.ui.screens

import MyBarChart
import MyLineChart
import MyPieChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cloudcare.fitdiary.data.model.ChartMode
import com.cloudcare.fitdiary.data.model.HealthEntry
import com.cloudcare.fitdiary.data.model.SettingsDataStore
import com.cloudcare.fitdiary.data.repository.HealthRepository
import com.cloudcare.fitdiary.ui.components.HealthStats
import com.cloudcare.fitdiary.ui.components.HorizontalChartPager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale


@Composable
fun DashboardScreen(healthRepository: HealthRepository) {
    val context = LocalContext.current
    val settings = remember { SettingsDataStore(context) }

    val chartModeState = produceState(initialValue = ChartMode.HORIZONTAL) {
        settings.getChartMode.collect { value = it }
    }

    val today = LocalDate.now().toString()
    var entry by remember { mutableStateOf<HealthEntry?>(null) }
    var entries by remember { mutableStateOf<List<HealthEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        healthRepository.getHealthEntries(today, today, { entries ->
            entry = entries.firstOrNull()
            isLoading = false
        }, {
            isLoading = false
        })

        healthRepository.getHealthEntries(
            LocalDate.now().minusDays(30).toString(),
            LocalDate.now().toString(),
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Today's Health Summary", style = MaterialTheme.typography.headlineMedium)
        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
            }

            else -> {
                entry?.let { HealthStats(it) } ?: Text("No data for today")
            }
        }


        // Placeholder for MPAndroidChart (requires View-based integration)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Health Analytics",
            style = MaterialTheme.typography.headlineMedium
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val dateInputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateOutputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
            val dateLabels = entries.map { entry ->
                try {
                    dateInputFormat.parse(entry.date)?.let { dateOutputFormat.format(it) }
                        ?: entry.date
                } catch (e: Exception) {
                    entry.date
                }
            }
            val waterValues = entries.map { entry -> entry.waterIntake }
            val weightValues = entries.map { entry -> entry.weight }
            val sleepValues = entries.map { entry -> entry.sleepHours }
            val stepValues = entries.map { entry -> entry.steps.toFloat() }
            val moodValues = entries.map { entry -> entry.mood }

            val pages = listOf<@Composable () -> Unit>(
                {
                    MyLineChart(
                        title = "Water Intake (ml)",
                        values = waterValues,
                        labels = dateLabels,
                        lineColor = Color.Blue,
                        yAxisFormatter = { value -> "${"%.1f".format(value)}ml" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                },
                {
                    MyLineChart(
                        title = "Weight (kg)",
                        values = weightValues,
                        labels = dateLabels,
                        lineColor = Color.Magenta,
                        yAxisFormatter = { value -> "${"%.1f".format(value)}kg" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                },
                {
                    MyBarChart(
                        title = "Sleep (hours)",
                        values = sleepValues,
                        labels = dateLabels,
                        barColor = Color.Cyan,
                        yAxisFormatter = { value -> "${"%.1f".format(value)}h" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                },
                {
                    MyBarChart(
                        title = "Steps",
                        values = stepValues,
                        labels = dateLabels,
                        barColor = Color.Yellow,
                        yAxisFormatter = { value -> "${value.toInt()}" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                },
                {
                    MyPieChart(
                        title = "Mood",
                        values = moodValues,
                        valueFormat = { value -> "${value.toInt()}" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            )

            when {
                chartModeState.value == ChartMode.HORIZONTAL -> {
                    HorizontalChartPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        pages = pages
                    )
                }

                else -> {
                    for (page in pages) {
                        page()
                    }
                }
            }

            /*
             MyLineChart(
                 title = "Water Intake (ml)",
                 values = waterValues,
                 labels = dateLabels,
                 lineColor = Color.Blue,
                 yAxisFormatter = { value -> "${"%.1f".format(value)}ml" },
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             )

             MyLineChart(
                 title = "Weight (kg)",
                 values = weightValues,
                 labels = dateLabels,
                 lineColor = Color.Magenta,
                 yAxisFormatter = { value -> "${"%.1f".format(value)}kg" },
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             )

             MyBarChart(
                 title = "Sleep (hours)",
                 values = sleepValues,
                 labels = dateLabels,
                 barColor = Color.Cyan,
                 yAxisFormatter = { value -> "${"%.1f".format(value)}h" },
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             )

             MyBarChart(
                 title = "Steps",
                 values = stepValues,
                 labels = dateLabels,
                 barColor = Color.Yellow,
                 yAxisFormatter = { value -> "${value.toInt()}" },
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             )


             MyPieChart(
                 title = "Mood",
                 values = moodValues,
                 valueFormat = { value -> "${value.toInt()}" },
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             )
            */
        }
    }
}
