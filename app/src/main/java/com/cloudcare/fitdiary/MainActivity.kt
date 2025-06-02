package com.cloudcare.fitdiary

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cloudcare.fitdiary.data.model.SettingsDataStore
import com.cloudcare.fitdiary.data.model.ThemeMode
import com.cloudcare.fitdiary.ui.components.AppNavigation
import com.cloudcare.fitdiary.ui.components.BottomNavigationBar
import com.cloudcare.fitdiary.ui.theme.FitDiaryTheme
import com.cloudcare.fitdiary.worker.WorkScheduler
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = setOf(
            HealthPermission.getReadPermission(StepsRecord::class)
        )

        // Create the permissions launcher
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
                // Permissions successfully granted
            } else {
                // Lack of required permissions
            }
        }

        requestPermissions.launch(permissions)


        WorkScheduler.scheduleDailyReminder(this, LocalTime.of(20, 0))
        enableEdgeToEdge()
        setContent { MainActivityContent() }
    }
}

@Composable
fun MainActivityContent() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val isGranted = ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                permissionLauncher.launch(permission)
            }
        }
    }

    val settings = SettingsDataStore(context)
    val themeMode by produceState(initialValue = ThemeMode.SYSTEM_DEFAULT) {
        settings.getThemeMode.collect { value = it }
    }

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

    val navController = rememberNavController()
    FitDiaryTheme(darkTheme = isDarkTheme) {
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