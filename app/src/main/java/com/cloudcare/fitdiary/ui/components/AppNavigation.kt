package com.cloudcare.fitdiary.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cloudcare.fitdiary.data.repository.FirebaseAuthRepository
import com.cloudcare.fitdiary.data.repository.FirebaseHealthRepository
import com.cloudcare.fitdiary.ui.screens.AddEntryScreen
import com.cloudcare.fitdiary.ui.screens.DashboardScreen
import com.cloudcare.fitdiary.ui.screens.HistoryScreen
import com.cloudcare.fitdiary.ui.screens.LoginScreen
import com.cloudcare.fitdiary.ui.screens.SettingsScreen
import com.cloudcare.fitdiary.ui.screens.SignupScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    val healthRepository = FirebaseHealthRepository()
    val authRepository = FirebaseAuthRepository()
    var isAuthenticated by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "dashboard" else "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                authRepository = authRepository,
                onLoginSuccess = { isAuthenticated = true }
            )
        }
        composable("signup") {
            SignupScreen(
                navController = navController,
                authRepository = authRepository,
                onSignupSuccess = { isAuthenticated = true }
            )
        }
        composable("dashboard") {

            DashboardScreen(healthRepository)
        }
        composable("history") {

            HistoryScreen(healthRepository)
        }
        composable("add_entry") {

            AddEntryScreen(healthRepository)
        }
        composable("settings") {

            SettingsScreen(authRepository)
        }
    }
}