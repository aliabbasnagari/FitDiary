package com.cloudcare.fitdiary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cloudcare.fitdiary.data.repository.MockHealthRepository
import com.cloudcare.fitdiary.ui.screens.HistoryScreen

@Composable
fun ScreenContainer(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Column() {
        Column(modifier = Modifier.weight(1f)) {
            content()
        }
        BottomNavigationBar(navController)
    }
}


@Preview(showBackground = true)
@Composable
fun ScreenContainerPreview() {
    ScreenContainer(
        rememberNavController()
    ) {
        HistoryScreen(
            healthRepository = MockHealthRepository()
        )
    }
}
