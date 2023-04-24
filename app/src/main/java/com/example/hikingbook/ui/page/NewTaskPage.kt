package com.example.hikingbook.ui.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.tool.toStringDate
import com.example.hikingbook.ui.component.CustomTextField
import com.example.hikingbook.ui.component.OperationButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.Route

const val ONE_DAY = 86400 * 1000

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewTaskPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {


    val focusManager = LocalFocusManager.current
    val requester = FocusRequester()

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }


//    val createdPicker = rememberDatePickerState(
//        initialDisplayedMonthMillis = System.currentTimeMillis()
//    )
//    val duePicker = rememberDatePickerState(
//        initialDisplayedMonthMillis = System.currentTimeMillis() + 86400 * 1000
//    )
    var createdDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    var dueDate by remember {
        mutableStateOf(System.currentTimeMillis() + ONE_DAY)
    }
    var isDialogShowing by remember {
        mutableStateOf(false)
    }
    var pickerType by remember {
        mutableStateOf(CreatedOrDue.NONE)
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
        CustomTextField(
            value = title,
            onValueChange = { title = it },
            label = "Title",
            modifier = Modifier.focusRequester(requester)
        )
        CustomTextField(
            value = description,
            onValueChange = { description = it },
            label = "Description"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CustomTextField(value = createdDate.toStringDate(), onValueChange = { }, label = "Created Date") {
                pickerType = CreatedOrDue.CREATED
                isDialogShowing = true
            }
            CustomTextField(value = dueDate.toStringDate(), onValueChange = { title = it }, label = "Due Date") {
                pickerType = CreatedOrDue.DUE
                isDialogShowing = true
            }
        }
        CustomTextField(value = title, onValueChange = { title = it }, label = "Location")

        Spacer(modifier = Modifier.weight(1f))
        OperationButton(
            clickOK = {
                viewModel.route = Route.MAIN
            },
            clickCancel = {
                viewModel.route = Route.MAIN
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
            confirmButton = { TextButton(onClick = {
                when(pickerType) {
                    CreatedOrDue.DUE -> { dueDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis() }
                    else -> { createdDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()}
                }
                isDialogShowing = false
            }) {
                Text(text = "OK")
            } },
            dismissButton = { TextButton(onClick = { isDialogShowing = false }) {
                Text(text = "Cancel")
            }}
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(Unit) {
        Log.d("!!!", "requestFocus")
        requester.requestFocus()
    }
}

enum class CreatedOrDue {
    CREATED, DUE, NONE
}