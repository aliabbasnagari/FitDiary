package com.cloudcare.fitdiary.data.repository


class MockAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return if (email.isNotBlank() && password.isNotBlank()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun signup(email: String, password: String): Result<Unit> {
        return if (email.isNotBlank() && password.length >= 6) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Weak password or invalid email"))
        }
    }

    override fun logout() {
        // No-op for mock
    }
}