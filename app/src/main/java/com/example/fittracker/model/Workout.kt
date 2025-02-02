package com.example.fittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val duration: Int,
    val caloriesBurned: Int,
    val date: Long,
    var isCompleted: Boolean = false
)
