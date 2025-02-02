package com.example.fittracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fittracker.ui.workouts.WorkoutViewModel
import com.example.fittracker.model.Workout
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fittracker.notifications.WorkoutReminderWorker
import java.util.concurrent.TimeUnit


@Composable
fun HomeScreen(navController: NavController, viewModel: WorkoutViewModel = viewModel()) {
    val scheduledWorkouts by viewModel.scheduledWorkouts.observeAsState(emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Upcoming Workouts",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (scheduledWorkouts.isEmpty()) {
            Text(
                text = "No workouts scheduled",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(scheduledWorkouts) { workout ->
                    WorkoutItem(
                        workout = workout,
                        onWorkoutCompleted = { viewModel.markWorkoutAsCompleted(it) },
                        onWorkoutDeleted = { viewModel.deleteWorkout(it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("scheduleWorkout") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Schedule a Workout")
        }
        //TestWorkNotificationButton()
    }
}

@Composable
fun TestWorkNotificationButton() {
    val context = LocalContext.current

    Button(onClick = {
        val workRequest = OneTimeWorkRequestBuilder<WorkoutReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }) {
        Text("Test Workout Notification")
    }
}


@Composable
fun WorkoutItem(workout: Workout, onWorkoutCompleted: (Workout) -> Unit, onWorkoutDeleted: (Workout) -> Unit) {
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
                Text(
                    text = "Duration: ${workout.duration} mins",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Calories: ${workout.caloriesBurned} kcal",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                Checkbox(
                    checked = workout.isCompleted,
                    onCheckedChange = { onWorkoutCompleted(workout) }
                )
                IconButton(onClick = { onWorkoutDeleted(workout) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete workout")
                }
            }
        }
    }
}