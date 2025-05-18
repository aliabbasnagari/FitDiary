package com.cloudcare.fitdiary.data.repository

import com.cloudcare.fitdiary.data.model.HealthEntry

interface HealthRepository {
    fun saveHealthEntry(entry: HealthEntry, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    fun getHealthEntries(
        startDate: String,
        endDate: String,
        onSuccess: (List<HealthEntry>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}