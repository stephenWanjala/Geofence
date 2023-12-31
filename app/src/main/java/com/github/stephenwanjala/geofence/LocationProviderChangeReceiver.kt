package com.github.stephenwanjala.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build


class LocationProviderChangedReceiver(
    private val viewModel: GeofenceViewModel
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val locationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isLocationEnabled) viewModel.onEvent(GeofenceEvent.LocationEnabled)
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION ||
            intent?.action == LocationManager.MODE_CHANGED_ACTION) {
            // Location provider && or  mode status has changed


            if (isLocationEnabled) {
                // Location services are enabled, send LocationEnabled event to ViewModel
                viewModel.onEvent(GeofenceEvent.LocationEnabled)
            } else {
                // Location services are disabled, send LocationDisabled event to ViewModel
                viewModel.onEvent(GeofenceEvent.LocationDisabled)
//                openLocationSettings(context)
            }
        }
    }
}
