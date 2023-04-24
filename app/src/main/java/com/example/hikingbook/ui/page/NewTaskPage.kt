package com.example.hikingbook.ui.page

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.tool.toStringDate
import com.example.hikingbook.ui.component.CustomTextField
import com.example.hikingbook.ui.component.OperationButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.Route
import com.example.hikingbook.tool.toStringRepresentation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

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
        mutableStateOf("")
    }
    var titleIsError by remember {
        mutableStateOf(false)
    }

    var description by remember {
        mutableStateOf("")
    }
    var descriptionIsError by remember {
        mutableStateOf(false)
    }


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
            modifier = Modifier.focusRequester(requester),

            isError = titleIsError,
            notError = { titleIsError = false }
        )
        CustomTextField(
            value = description,
            onValueChange = { description = it },
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
            viewModel.route = Route.MAP
        }

        Spacer(modifier = Modifier.weight(1f))
        OperationButton(
            clickOK = {
                if (title.isBlank()) {
                    titleIsError = true
                }
                if (description.isBlank()) {
                    descriptionIsError = true
                }
                if (title.isNotBlank() && description.isNotBlank()) {
                    // insert
                    viewModel.route = Route.MAIN
                }
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

    LaunchedEffect(Unit) {
        Log.d("!!!", "requestFocus")
        requester.requestFocus()
    }
}

enum class CreatedOrDue {
    CREATED, DUE, NONE
}

private fun Context.getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    location: (LatLng) -> Unit
) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
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
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            // Use the latLng for your needs

            location(latLng)
        }
    }.addOnFailureListener {
        // Handle the failure case
    }
}