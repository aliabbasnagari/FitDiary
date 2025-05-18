package com.cloudcare.fitdiary.data.repository

import com.cloudcare.fitdiary.data.model.HealthEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HealthRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveHealthEntry(entry: HealthEntry, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("health_entries").document(entry.date)
            .set(entry)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getHealthEntries(
        startDate: String,
        endDate: String,
        onSuccess: (List<HealthEntry>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("health_entries")
            .whereGreaterThanOrEqualTo("date", startDate)
            .whereLessThanOrEqualTo("date", endDate)
            .get()
            .addOnSuccessListener { result ->
                val entries = result.toObjects(HealthEntry::class.java)
                onSuccess(entries)
            }
            .addOnFailureListener { onFailure(it) }
    }
}