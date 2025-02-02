package com.example.fittracker.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fittracker.model.Workout

@Composable
fun AddWorkoutScreen(
    viewModel: WorkoutViewModel = viewModel(),
    onWorkoutAdded: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add New Workout", style = MaterialTheme.typography.headlineMedium)

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

        Button(
            onClick = {
                if (name.isNotEmpty() && duration.isNotEmpty() && calories.isNotEmpty()) {
                    val workout = Workout(
                        name = name,
                        duration = duration.toIntOrNull() ?: 0,
                        caloriesBurned = calories.toIntOrNull() ?: 0,
                        date = System.currentTimeMillis()
                    )
                    viewModel.insert(workout)
                    onWorkoutAdded()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Workout")
        }
    }
}
