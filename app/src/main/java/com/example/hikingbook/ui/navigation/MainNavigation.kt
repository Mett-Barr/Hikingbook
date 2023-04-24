package com.example.hikingbook.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.*
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.ui.MainPage
import com.example.hikingbook.ui.page.NewTaskPage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.Route

@Composable
fun MainNavigation(
    viewModel: MainViewModel = viewModel()
) {

    AnimatedContent(targetState = viewModel.route) {
        when (it) {
            Route.MAIN -> {
                MainPage()
            }
            Route.NEW_TASK -> {
                NewTaskPage()
            }
        }
    }

    BackHandler(viewModel.route == Route.NEW_TASK) {
        viewModel.route = Route.MAIN
    }
}

