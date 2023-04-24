package com.example.hikingbook.ui.page

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.R
import com.example.hikingbook.Route
import com.example.hikingbook.ui.component.ClickableIcon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Preview
@Composable
fun Map(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {



//    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(viewModel.location, 10f)
    }

    var markerPosition by remember {
        mutableStateOf(viewModel.location)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            onMapClick = {
                Log.d("!!!", it.toString())
                markerPosition = it
            }
        ) {
            Marker(
                state = MarkerState(position = markerPosition),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }

        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding())) {
            Row(modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ClickableIcon(painter = painterResource(id = R.drawable.arrow_back_fill0_wght400_grad0_opsz48)) {
                    viewModel.navigateBack()
                }
                ClickableIcon(painter = painterResource(id = R.drawable.done_fill0_wght400_grad0_opsz48)) {
                    viewModel.location = markerPosition
                    viewModel.navigateBack()
                }
            }
        }
    }
}