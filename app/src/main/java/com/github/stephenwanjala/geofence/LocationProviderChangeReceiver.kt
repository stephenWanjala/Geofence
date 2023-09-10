package com.github.stephenwanjala.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager


class LocationProviderChangedReceiver(
    private val viewModel: GeofenceViewModel
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            // Location provider status has changed
            val locationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (isLocationEnabled) {
                // Location services are enabled, send LocationEnabled event to ViewModel
                viewModel.onEvent(GeofenceEvent.LocationEnabled)
            } else {
                // Location services are disabled, send LocationDisabled event to ViewModel
                viewModel.onEvent(GeofenceEvent.LocationDisabled)
                openLocationSettings(context)
            }
        }
    }
}
