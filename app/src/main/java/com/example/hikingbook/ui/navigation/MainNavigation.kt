package com.example.hikingbook.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.ui.MainPage
import com.example.hikingbook.ui.page.NewTaskPage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.Route
import com.example.hikingbook.ui.page.Map
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

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
            Route.MAP -> {
                Map()
            }
        }
    }

    BackHandler(viewModel.route != Route.MAIN) {
        if (viewModel.route == Route.NEW_TASK) {
            viewModel.route = Route.MAIN
        } else if (viewModel.route == Route.MAP) {
            viewModel.route = Route.NEW_TASK
        }
    }
}

