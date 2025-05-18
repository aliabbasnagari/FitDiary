package com.cloudcare.fitdiary.data.model

data class HealthEntry(
    val date: String,
    val waterIntake: Float,
    val sleepHours: Float,
    val steps: Int,
    val mood: String,
    val weight: Float
)