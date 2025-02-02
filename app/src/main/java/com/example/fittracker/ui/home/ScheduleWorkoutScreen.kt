package com.example.fittracker.ui.home

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.*
import com.example.fittracker.model.Workout
import com.example.fittracker.notifications.WorkoutReminderWorker
import com.example.fittracker.ui.workouts.WorkoutViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleWorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = viewModel(),
    onWorkoutScheduled: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var selectedDate by remember { mutableStateOf("${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}") }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var selectedTime by remember { mutableStateOf("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}") }
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Schedule a Workout", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { datePickerDialog.show() }) {
            Text("Select Date")
        }
        Text(text = "Selected Date: $selectedDate", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { timePickerDialog.show() }) {
            Text("Select Time")
        }
        Text(text = "Selected Time: $selectedTime", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isEmpty()) {
                    println("Workout name cannot be empty")
                    return@Button
                }

                val durationInt = duration.toIntOrNull()
                val caloriesInt = calories.toIntOrNull()

                if (durationInt == null || durationInt <= 0) {
                    println("Please enter a valid duration in minutes")
                    return@Button
                }

                if (caloriesInt == null || caloriesInt <= 0) {
                    println("Please enter a valid calorie value")
                    return@Button
                }

                val workout = Workout(
                    name = name,
                    duration = durationInt,
                    caloriesBurned = caloriesInt,
                    date = calendar.timeInMillis
                )

                viewModel.insert(workout)

                val delayInMillis = calendar.timeInMillis - System.currentTimeMillis()
                val workRequest = OneTimeWorkRequestBuilder<WorkoutReminderWorker>()
                    .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)

                onWorkoutScheduled()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Schedule Workout")
        }

    }
}

