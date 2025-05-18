package com.cloudcare.fitdiary.data.model

import com.google.firebase.firestore.DocumentId

data class HealthEntry(
    @DocumentId val id: String = "",
    val date: String = "",
    val waterIntake: Float = 0f,
    val sleepHours: Float = 0f,
    val steps: Int = 0,
    val mood: String = "",
    val weight: Float = 0f
)