package com.example.fittracker.data

import com.example.fittracker.model.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    val allWorkouts: Flow<List<Workout>> = workoutDao.getAllWorkouts()
    val scheduledWorkouts: Flow<List<Workout>> = workoutDao.getScheduledWorkouts()
    val completedWorkouts: Flow<List<Workout>> = workoutDao.getCompletedWorkouts()

    suspend fun insert(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun markWorkoutAsCompleted(workout: Workout) {
        workout.isCompleted = true
        workoutDao.updateWorkout(workout)
    }

    suspend fun update(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun deleteWorkoutById(workoutId: Int) {
        workoutDao.deleteWorkoutById(workoutId)
    }
}
