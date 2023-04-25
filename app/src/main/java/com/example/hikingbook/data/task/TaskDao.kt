package com.example.hikingbook.data.task

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface TaskDao {

    // basic
    @Query("SELECT * FROM task ORDER BY dueDate DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)


    // other
    @Query("SELECT * FROM task WHERE title LIKE :searchQuery ORDER BY dueDate DESC")
    fun searchTasks(searchQuery: String): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY dueDate DESC")
    fun pagingTasks(): PagingSource<Int, Task>
}
