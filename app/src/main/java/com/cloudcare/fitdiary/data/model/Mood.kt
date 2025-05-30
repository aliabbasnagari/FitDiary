package com.cloudcare.fitdiary.data.model

enum class Mood(val emoji: String, val displayName: String, val value: Float) {
    HAPPY("😊", "Happy", 2f),
    NEUTRAL("😐", "Neutral", 1f),
    SAD("😔", "Sad", 0f);

    companion object {
        fun fromEmoji(emoji: String): Mood = entries.find { it.emoji == emoji } ?: SAD
    }
}