package com.example.fittracker.ui.workouts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fittracker.data.WorkoutDatabase
import com.example.fittracker.data.WorkoutRepository
import com.example.fittracker.model.Workout
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var repository: WorkoutRepository
    val scheduledWorkouts: LiveData<List<Workout>>
    val completedWorkouts: LiveData<List<Workout>>

    val allWorkouts by lazy {
        repository.allWorkouts
    }

    init {
        val workoutDao = WorkoutDatabase.getDatabase(application).workoutDao()
        repository = WorkoutRepository(workoutDao)
        scheduledWorkouts = repository.scheduledWorkouts.asLiveData()
        completedWorkouts = repository.completedWorkouts.asLiveData()
    }

    fun insert(workout: com.example.fittracker.model.Workout) = viewModelScope.launch {
        repository.insert(workout)
    }

    fun update(workout: com.example.fittracker.model.Workout) = viewModelScope.launch {
        repository.update(workout)
    }

    fun deleteWorkout(workout: Workout) = viewModelScope.launch {
        repository.deleteWorkout(workout)
    }

    fun deleteWorkoutById(workoutId: Int) = viewModelScope.launch {
        repository.deleteWorkoutById(workoutId)
    }


    fun markWorkoutAsCompleted(workout: Workout) = viewModelScope.launch {
        repository.markWorkoutAsCompleted(workout)
    }


}
