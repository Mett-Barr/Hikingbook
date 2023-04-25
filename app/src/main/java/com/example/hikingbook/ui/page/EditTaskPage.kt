package com.example.hikingbook.ui.page

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.example.hikingbook.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.R
import com.example.hikingbook.Route
import com.example.hikingbook.data.task.Task
import com.example.hikingbook.tool.toStringDate
import com.example.hikingbook.tool.toStringRepresentation
import com.example.hikingbook.ui.component.ClickableIcon
import com.example.hikingbook.ui.component.CustomTextField
import com.example.hikingbook.ui.component.OperationButton
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
//    task: Task
) {


    val focusManager = LocalFocusManager.current
    val requester = FocusRequester()


    val context = LocalContext.current

    val client = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    context.getLastLocation(
        client
    ) {
        viewModel.location = it
    }


    var title by remember {
        mutableStateOf(viewModel.editedTask.title)
    }
    var titleIsError by remember {
        mutableStateOf(false)
    }

    var description by remember {
        mutableStateOf(viewModel.editedTask.description)
    }
    var descriptionIsError by remember {
        mutableStateOf(false)
    }


    var createdDate by remember {
        mutableStateOf(viewModel.editedTask.createdDate)
    }
    var dueDate by remember {
        mutableStateOf(viewModel.editedTask.dueDate)
    }
    var isDialogShowing by remember {
        mutableStateOf(false)
    }
    var pickerType by remember {
        mutableStateOf(CreatedOrDue.NONE)
    }

    var isDeleteDialog by remember {
        mutableStateOf(false)
    }


    Column(
        modifier
            .systemBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Edit Task",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center
        )
        CustomTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = "Task Title",
            label = "Title",
            modifier = Modifier.focusRequester(requester),

            isError = titleIsError,
            notError = { titleIsError = false }
        )
        CustomTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = "Task Description",
            label = "Description",

            isError = descriptionIsError,
            notError = { descriptionIsError = false }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CustomTextField(
                value = createdDate.toStringDate(),
                onValueChange = { },
                label = "Created Date"
            ) {
                pickerType = CreatedOrDue.CREATED
                isDialogShowing = true
            }
            CustomTextField(
                value = dueDate.toStringDate(),
                onValueChange = { },
                label = "Due Date"
            ) {
                pickerType = CreatedOrDue.DUE
                isDialogShowing = true
            }
        }
        CustomTextField(
            value = viewModel.location.toStringRepresentation(),
            onValueChange = { },
            label = "Location"
        ) {
            viewModel.navigate(Route.MAP)
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                isDeleteDialog = true
            }
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.delete_fill0_wght400_grad0_opsz48),
                contentDescription = "",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.errorContainer)
            )
        }
        OperationButton(
            clickOK = {
                if (title.isBlank()) {
                    titleIsError = true
                }
                if (description.isBlank()) {
                    descriptionIsError = true
                }
                if (title.isNotBlank() && description.isNotBlank()) {
                    // update
                    viewModel.updateTask(
                        viewModel.editedTask.copy(
                            title = title,
                            description = description,
                            createdDate = createdDate,
                            dueDate = dueDate,
                            locationCoordinate = viewModel.location.toStringRepresentation()
                        )
                    )
                    viewModel.navigateBack()
                }
            },
            clickCancel = {
                viewModel.navigateBack()
            }
        )
    }

    if (isDialogShowing) {
        focusManager.clearFocus()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = when (pickerType) {
                CreatedOrDue.DUE -> dueDate
                else -> createdDate
            }
        )

        DatePickerDialog(onDismissRequest = { isDialogShowing = false },
            confirmButton = {
                TextButton(onClick = {
                    when (pickerType) {
                        CreatedOrDue.DUE -> {
                            dueDate =
                                datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            if (dueDate < createdDate) createdDate = dueDate - ONE_DAY
                        }
                        else -> {
                            createdDate =
                                datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            if (createdDate > dueDate) dueDate = createdDate + ONE_DAY
                        }
                    }
                    isDialogShowing = false
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogShowing = false }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (isDeleteDialog) {
        AlertDialog(
            onDismissRequest = { isDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDeleteDialog = false
                        viewModel.deleteTask(viewModel.editedTask)
                        viewModel.navigateBack()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        isDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Delete Task") },
        )
    }

    LaunchedEffect(Unit) {
        if (title.isBlank()) {
            requester.requestFocus()
        }
    }

}