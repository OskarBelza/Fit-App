package com.example.fittracker

import SettingsScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fittracker.notifications.NotificationHelper
import com.example.fittracker.ui.home.HomeScreen
import com.example.fittracker.ui.home.ScheduleWorkoutScreen
import com.example.fittracker.ui.workouts.AddWorkoutScreen
import com.example.fittracker.ui.workouts.WorkoutViewModel
import com.example.fittracker.ui.workouts.WorkoutsScreen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitTrackerApp()
        }
        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()
        android.util.Log.d("NotificationHelper", "Notification channel created")

    }
}


@Composable
fun FitTrackerApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "home") {
                HomeScreen(navController = navController)
            }

            composable(route = "workouts") {
                WorkoutsScreen(navController = navController)
            }
            composable("settings") { SettingsScreen() }
            composable(route = "addWorkout") {
                AddWorkoutScreen(onWorkoutAdded = {
                    navController.popBackStack()
                })
            }
            composable(route = "scheduleWorkout") {
                ScheduleWorkoutScreen(navController = navController, onWorkoutScheduled = {
                    navController.popBackStack()
                })
            }


        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            label = { Text("Workouts") },
            selected = false,
            onClick = { navController.navigate("workouts") },
            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") }
        )
        NavigationBarItem(
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
        )
    }
}
