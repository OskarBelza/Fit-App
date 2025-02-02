package com.example.fittracker.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fittracker.model.Workout
import kotlinx.coroutines.launch

@Composable
fun WorkoutsScreen(navController: NavController, viewModel: WorkoutViewModel = viewModel()) {
    val workouts by viewModel.allWorkouts.collectAsState(initial = emptyList())
    val completedWorkouts by viewModel.completedWorkouts.observeAsState(emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addWorkout")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Workout")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                text = "Completed Workouts",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (completedWorkouts.isEmpty()) {
                Text("No workouts completed yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(completedWorkouts) { workout ->
                        WorkoutItem(
                            workout = workout,
                            onWorkoutDeleted = {
                                viewModel.deleteWorkout(it)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Workout deleted")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutItem(workout: Workout, onWorkoutDeleted: (Workout) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onWorkoutDeleted(workout)
                    showDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Workout") },
            text = { Text("Are you sure you want to delete ${workout.name}?") }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = workout.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Duration: ${workout.duration} mins", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Calories: ${workout.caloriesBurned} kcal", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete workout")
            }
        }
    }
}

