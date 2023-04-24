package com.example.hikingbook.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hikingbook.tool.toLongDate

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    val title: String,
    val description: String,
    val createdDate: Long,
    val dueDate: Long,
    val locationCoordinate: String
)

object TaskObj {
    val obj = Task(
        title = "title",
        description = "description",
        createdDate = "2023/04/24".toLongDate(),
        dueDate = "2023/04/25".toLongDate(),
        locationCoordinate = "25.0174719, 121.3662922"
    )
}