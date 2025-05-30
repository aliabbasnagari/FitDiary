package com.cloudcare.fitdiary.data.model

enum class HealthField(val displayName: String, val unitFormatter: (Float) -> String) {
    WATER_INTAKE("Water Intake", { value -> "${"%.1f".format(value)}ml" }),
    SLEEP_HOURS("Sleep Hours", { value -> "${"%.1f".format(value)}h" }),
    STEPS("Steps", { value -> "${value.toInt()}" }),
    WEIGHT("Weight", { value -> "${"%.1f".format(value)}kg" }),
    MOOD("Mood", { value ->
        when (value) {
            Mood.HAPPY.value -> Mood.HAPPY.emoji
            Mood.NEUTRAL.value -> Mood.NEUTRAL.emoji
            Mood.SAD.value -> Mood.SAD.emoji
            else -> value.toString()
        }
    })
}