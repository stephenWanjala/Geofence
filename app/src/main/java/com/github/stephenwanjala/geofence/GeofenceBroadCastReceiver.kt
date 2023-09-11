package com.github.stephenwanjala.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get the GeofencingEvent from the intent
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        // Check if the event is a geofencing transition event
        if (geofencingEvent?.hasError() == false) {
            // Get the geofence transition type
            val geofenceTransition = geofencingEvent.geofenceTransition

            // Get the geofence that was triggered
            val triggeringGeofence = geofencingEvent.triggeringGeofences

            // Do something based on the geofence transition type
            when (geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    // The user entered the geofenced area
                    Log.d("GeofenceBroadcastReceiver", "Entered geofenced area")
                    if (triggeringGeofence != null) {
                        for (geofence in triggeringGeofence) {
                            Log.d(
                                "GeofenceBroadcastReceiver",
                                "Entered geofence ${geofence.requestId}"
                            )
                        }
                    }
                }

                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    // The user exited the geofenced area
                    Log.d("GeofenceBroadcastReceiver", "Exited geofenced area")
                    if (triggeringGeofence != null) {
                        for (geofence in triggeringGeofence) {
                            Log.d(
                                "GeofenceBroadcastReceiver",
                                "Exited geofence ${geofence.requestId}"
                            )
                        }
                    }
                }
            }
        }
    }
}
