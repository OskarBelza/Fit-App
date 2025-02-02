package com.example.fittracker.data

import androidx.room.*
import com.example.fittracker.model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Int)

    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE isCompleted = 0 ORDER BY date ASC")
    fun getScheduledWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE isCompleted = 1 ORDER BY date DESC")
    fun getCompletedWorkouts(): Flow<List<Workout>>

}
