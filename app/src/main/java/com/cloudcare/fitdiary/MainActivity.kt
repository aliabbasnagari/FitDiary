package com.cloudcare.fitdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cloudcare.fitdiary.ui.components.AppNavigation
import com.cloudcare.fitdiary.ui.components.BottomNavigationBar
import com.cloudcare.fitdiary.ui.theme.FitDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainActivityContent() }
    }
}

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()
    FitDiaryTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentRoute !in listOf("signup", "login")) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}