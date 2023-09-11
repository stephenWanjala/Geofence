package com.github.stephenwanjala.geofence

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GeofenceViewModel @Inject constructor() : ViewModel() {
    private val locationEnabledState = MutableStateFlow(LocationEnabledState())
    val locationEnabled = locationEnabledState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500),
        LocationEnabledState()
    )



    fun onEvent(event: GeofenceEvent) {
        when (event) {
            is GeofenceEvent.LocationEnabled -> {
                locationEnabledState.value = LocationEnabledState(true)
            }

            is GeofenceEvent.LocationDisabled -> {
                locationEnabledState.value = LocationEnabledState(false)
            }
        }
    }


}


data class LocationEnabledState(val isLocationEnabled: Boolean = false)

sealed class GeofenceEvent {
    data object LocationEnabled : GeofenceEvent()
    data object LocationDisabled : GeofenceEvent()
}