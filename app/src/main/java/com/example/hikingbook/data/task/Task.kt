package com.example.hikingbook.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    val title: String,
    val description: String,
    val createdDate: String,
    val dueDate: String,
    val locationCoordinate: String
)