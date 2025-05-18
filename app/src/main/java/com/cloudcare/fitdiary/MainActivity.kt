package com.cloudcare.fitdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cloudcare.fitdiary.ui.screens.AddEntryScreen
import com.cloudcare.fitdiary.ui.theme.FitDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainActivityPreview() }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    FitDiaryTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column() {
                Greeting(
                    name = "Android",
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                )
                AddEntryScreen()
            }
        }
    }
}