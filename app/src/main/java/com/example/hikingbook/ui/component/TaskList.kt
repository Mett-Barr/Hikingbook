package com.example.hikingbook.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.Route
import com.example.hikingbook.tool.toLatLng

@Composable
fun PaddingValues.TaskList(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val tasks = viewModel.pagingTasks.collectAsLazyPagingItems()

    val horizonPadding = 8.dp

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            top = calculateTopPadding(),
            bottom = calculateBottomPadding(),
            start = horizonPadding,
            end = horizonPadding
        )
    ) {
        items(tasks) {
            if (it != null) {
                TaskCard(task = it) {
                    viewModel.editedTask = it
                    viewModel.location = viewModel.editedTask.locationCoordinate.toLatLng() ?: viewModel.singapore
                    viewModel.navigate(Route.EDIT_TASK)
                }
            }
        }
    }
}