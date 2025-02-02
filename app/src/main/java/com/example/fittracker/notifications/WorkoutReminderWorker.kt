package com.example.fittracker.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fittracker.data.WorkoutDatabase
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutReminderWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = WorkoutDatabase.getDatabase(applicationContext)
        val currentTime = System.currentTimeMillis()

        val workouts = database.workoutDao().getScheduledWorkouts().first()

        for (workout in workouts) {
            if (workout.date <= currentTime && !workout.isCompleted) {
                NotificationHelper(applicationContext).sendNotification(
                    "Scheduled Workout Reminder",
                    "Don't forget your workout: ${workout.name} scheduled at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(workout.date)}!"
                )
            }
        }

        return Result.success()
    }
}
