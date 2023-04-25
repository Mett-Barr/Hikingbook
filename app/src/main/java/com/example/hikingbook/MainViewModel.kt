package com.example.hikingbook

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.hikingbook.data.quote.QuoteResponse
import com.example.hikingbook.data.quote.RetrofitClient
import com.example.hikingbook.data.task.Task
import com.example.hikingbook.data.task.TaskDao
import com.example.hikingbook.data.task.TaskObj
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    // Room
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val pagingTasks: Flow<PagingData<Task>> = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        taskDao.pagingTasks()
    }.flow

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
    var quote by mutableStateOf(QuoteResponse())
    var quoteState by mutableStateOf(QuoteState.SUCCESS)
    var errorCode by mutableStateOf(-1)
    var errorMessage by mutableStateOf("")
    fun getRandomQuote() = viewModelScope.launch(Dispatchers.IO) {
        RetrofitClient.apiService.getRandomQuote().apply {
            if (isSuccessful) {
                body()?.also {
                    quote = it
                    quoteState = QuoteState.SUCCESS

                    Log.d("!!!", "getRandomQuote: isSuccessful $quote")
                }
            } else {
                quoteState = QuoteState.FAILURE
                Log.d("!!!", "getRandomQuote: FAILURE ${this.message()}  ${code()}")

                errorCode = code()
                errorMessage = message()
            }
        }
    }

    // Edit Task Page
    var editedTask = TaskObj.obj
}

enum class Route {
    MAIN, NEW_TASK, MAP, EDIT_TASK
}

enum class QuoteState {
    SUCCESS, FAILURE
}