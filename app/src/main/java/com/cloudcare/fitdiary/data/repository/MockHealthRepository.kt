package com.cloudcare.fitdiary.data.repository

import com.cloudcare.fitdiary.data.model.HealthEntry
import java.time.LocalDate

class MockHealthRepository : HealthRepository {
    private val mockEntries = listOf(
        HealthEntry(
            date = LocalDate.now().toString(),
            waterIntake = 2000f,
            sleepHours = 7.5f,
            steps = 10000,
            mood = "😊",
            weight = 70f
        ),
        HealthEntry(
            date = LocalDate.now().minusDays(1).toString(),
            waterIntake = 1800f,
            sleepHours = 6.5f,
            steps = 8000,
            mood = "😐",
            weight = 70.2f
        )
    )

    override fun saveHealthEntry(entry: HealthEntry, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        onSuccess() // Simulate success
    }

    override fun getHealthEntries(
        startDate: String,
        endDate: String,
        onSuccess: (List<HealthEntry>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(mockEntries.filter {
            it.date >= startDate && it.date <= endDate
        })
    }
}