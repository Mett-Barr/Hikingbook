package com.example.hikingbook

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.hikingbook.data.task.Task
import com.example.hikingbook.ui.MainPage
import com.example.hikingbook.ui.navigation.MainNavigation
import com.example.hikingbook.ui.page.Map
import com.example.hikingbook.ui.page.NewTaskPage
import com.example.hikingbook.ui.theme.HikingbookTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionRequestCode = 0

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HikingbookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")
//                    MainPage()
//                    NewTaskPage()
                    MainNavigation()
//                    Map()
                }
            }
        }


        checkAndRequestLocationPermission()
//        roomTest()
    }

    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can use the location now
            } else {
                // Permission denied, you can show an error message or handle it accordingly
            }
        }
    }

    private fun roomTest() {
        // 監聽所有待辦事項的變化
        viewModel.allTasks.observe(this@MainActivity) { tasks ->
            tasks.forEach { task ->
                Log.d("MainActivity", "Task: ${task.title}")
            }
        }

        // 插入隨機數據
        lifecycleScope.launch(Dispatchers.IO) {
            for (i in 1..5) {
                val newTask = Task(
                    title = "Test Task $i",
                    description = "This is test task $i.",
                    createdDate = "2023-04-${24 + i}",
                    dueDate = "2023-04-${25 + i}",
                    locationCoordinate = "25.0174719, 121.3662922"
                )
                viewModel.insertTask(newTask)
            }
        }

        // 等待數據插入後進行操作
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000) // 等待兩秒以確保數據已插入

            // 刪除第一個待辦事項（需要在 UI 中根據用戶操作觸發）
            viewModel.allTasks.value?.firstOrNull()?.let { firstTask ->
                viewModel.deleteTask(firstTask)
            }

            // 更新第二個待辦事項的標題（需要在 UI 中根據用戶操作觸發）
            viewModel.allTasks.value?.getOrNull(1)?.let { secondTask ->
                val updatedTask = secondTask.copy(title = "Updated Task 2")
                viewModel.updateTask(updatedTask)
            }
        }
    }
}