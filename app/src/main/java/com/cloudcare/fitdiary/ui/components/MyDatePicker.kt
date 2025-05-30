package com.cloudcare.fitdiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    title: String,
    initialDate: LocalDate = LocalDate.now(),
    lowerBound: LocalDate? = null,
    onDateSelected: (selectedDate: Long?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember {
        mutableStateOf(
            initialDate.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
    }
    val lowerBoundMillis =
        lowerBound?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(lowerBound) {
        lowerBoundMillis?.let { newLowerBound ->
            if (selectedDate != null && selectedDate!! < newLowerBound) {
                selectedDate = newLowerBound
                onDateSelected(newLowerBound)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(4.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .clickable { showDialog = true }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = selectedDate?.let { dateFormat.format(Date(it)) } ?: "select date",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedDate == null) MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select start date",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
            )
        }


        if (showDialog) {
            val datePickerState =
                rememberDatePickerState(selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return (lowerBoundMillis?.let { utcTimeMillis >= it } ?: true)
                    }
                })


            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDate = datePickerState.selectedDateMillis
                            showDialog = false
                            onDateSelected(selectedDate)
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(16.dp),
                    title = {
                        Text(
                            text = "Select Date",
                            modifier = Modifier.padding(8.dp)
                        )
                    })
            }
        }
    }
}