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
import com.example.hikingbook.data.task.TaskObj
import com.google.android.gms.maps.model.LatLng
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
    var lastRoute = Route.MAIN
    fun navigate(newRoute: Route) {
        lastRoute = route
        route = newRoute
    }
    fun navigateBack() {
        when (route) {
            Route.MAIN -> {}
            Route.NEW_TASK, Route.EDIT_TASK -> {
                route = Route.MAIN
            }
            Route.MAP -> {
                navigate(lastRoute)
            }
        }
    }

    val singapore = LatLng(1.35, 103.87)
    var location by mutableStateOf(singapore)

    // New Task Page


    // Edit Task Page
    var editedTask = TaskObj.obj
}

enum class Route {
    MAIN, NEW_TASK, MAP, EDIT_TASK
}