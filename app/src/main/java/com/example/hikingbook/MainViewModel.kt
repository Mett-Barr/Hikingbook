package com.example.hikingbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikingbook.data.task.Task
import com.example.hikingbook.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    // Room
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    fun insertTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
    }


    /** UI */
    var route by mutableStateOf(Route.MAIN)

    // New Task Page

}

enum class Route {
    MAIN, NEW_TASK
}