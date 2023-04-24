package com.example.hikingbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.hikingbook.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.data.task.Task
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hikingbook.Route
import com.example.hikingbook.tool.toLatLng
import com.example.hikingbook.ui.component.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    viewModel: MainViewModel = viewModel(),
) {

    val list by viewModel.allTasks.observeAsState()

//    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val horizonPadding = 8.dp


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.route = Route.NEW_TASK }) {
//                Icon(painter = painterResource(id = ), contentDescription = )
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = horizonPadding,
                end = horizonPadding
            )
//            contentPadding = paddingValues
//            contentPadding = PaddingValues(
//                top = systemBarsPadding.calculateTopPadding(),
//                bottom = systemBarsPadding.calculateBottomPadding(),
//                start = horizonPadding,
//                end = horizonPadding
//            )
        ) {
            items(list ?: emptyList()) {
                TaskCard(task = it) {
                    viewModel.editedTask = it
                    viewModel.location = viewModel.editedTask.locationCoordinate.toLatLng() ?: viewModel.singapore
                    viewModel.navigate(Route.EDIT_TASK)
                }
            }
        }
    }

}