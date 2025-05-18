package com.cloudcare.fitdiary.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseAuthRepository : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<Unit> = suspendCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { continuation.resume(Result.success(Unit)) }
            .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    }

    override suspend fun signup(email: String, password: String): Result<Unit> = suspendCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { continuation.resume(Result.success(Unit)) }
            .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    }

    override fun logout() {
        auth.signOut()
    }
}
