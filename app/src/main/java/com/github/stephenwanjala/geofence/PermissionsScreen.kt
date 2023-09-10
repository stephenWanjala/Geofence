@file:OptIn(ExperimentalPermissionsApi::class)

package com.github.stephenwanjala.geofence

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalPermissionsApi::class)
@Destination(start = true)
@Composable
fun PermissionsScreen(
    viewModel: GeofenceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationEnabledState = viewModel.locationEnabled.collectAsState()

    val receiver = LocationProviderChangedReceiver(viewModel)

    DisposableEffect(Unit) {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        ) else listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    LocationPermissionScreen(
        locationPermissionsState = locationPermissionsState,
        onGrantPermissionClick = {
            locationPermissionsState.launchMultiplePermissionRequest()
        }) {
        if (locationEnabledState.value.isLocationEnabled) {
            Text(text = "Location Turned on")
        } else {
            Button(onClick = { openLocationSettings(context) }) {
                Text(text = "Turn on Location")
            }
        }

    }

}


fun openLocationSettings(context: Context) {
    val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(locationSettingsIntent)
}


@Composable
fun LocationPermissionScreen(
    locationPermissionsState: MultiplePermissionsState,
    onGrantPermissionClick: () -> Unit,
    onPermissionsGranted: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (locationPermissionsState.allPermissionsGranted) {
            onPermissionsGranted()
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val allPermissionsRevoked =
                        locationPermissionsState.permissions.size ==
                                locationPermissionsState.revokedPermissions.size

                    val textToShow = if (!allPermissionsRevoked) {
                        "Yay! Thanks for letting me access your approximate location. " +
                                "But you know what would be great? If you allow me to know where you " +
                                "exactly are. Thank you!"
                    } else if (locationPermissionsState.shouldShowRationale) {
                        "Getting your exact location is important for this app. " +
                                "Please grant us fine location. Thank you"
                    } else {
                        "To Geofence Needs Location Permission. Allow in Settings"
                    }

                    val buttonText = if (!allPermissionsRevoked) {
                        "Allow precise location"
                    } else {
                        "Request permissions"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = textToShow,
                            textAlign = TextAlign.Center

                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onGrantPermissionClick) {
                        Text(buttonText)
                    }
                }
            }
        }
    }
}



