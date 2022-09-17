package com.zhdanon.mysights2.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.zhdanon.mysights2.data.model.CurrentSightDto
import com.zhdanon.mysights2.data.model.SightDto
import com.zhdanon.mysights2.domain.GetOTMUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GMapViewModel @Inject constructor(
    application: Application,
    private val getOTMUseCase: GetOTMUseCase
) : AndroidViewModel(application) {

    private var fusedClient: FusedLocationProviderClient? = null

    private val _location = Channel<Location>()
    val location = _location.receiveAsFlow()

    private var _currentLocation: LatLng? = null
    val currentLocation get() = _currentLocation

    private val _sightsList = Channel<List<SightDto>>()
    val sightsList = _sightsList.receiveAsFlow()

    val sightsMap = mutableMapOf<LatLng, String>()
    private val _currentSight = MutableSharedFlow<CurrentSightDto>()
    val currentSight = _currentSight.asSharedFlow()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                locationListener?.onLocationChanged(location)
                viewModelScope.launch(Dispatchers.IO) {
                    _location.send(location)
                    _currentLocation = LatLng(location.latitude, location.longitude)
                    val temp = getOTMUseCase.executeSights(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                    temp.map { sightsMap.put(LatLng(it.point.lat, it.point.lon), it.xid) }
                    _sightsList.send(temp)
                }
            }
        }
    }
    private var locationListener: LocationSource.OnLocationChangedListener? = null

    private var _gMap: GoogleMap? = null
    val gMap get() = _gMap

    init {
        fusedClient = LocationServices.getFusedLocationProviderClient(application)
    }

    fun setMap(googleMap: GoogleMap) {
        _gMap = googleMap
    }

    @SuppressLint("MissingPermission")
    fun startLocation() {
        val request = LocationRequest.create()
            .setInterval(1_000)
            .setSmallestDisplacement(15f)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        fusedClient?.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun getSightInfo(locate: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            val xid: String? = sightsMap[locate]
            if (xid != null) {
                _currentSight.emit(getOTMUseCase.executeSightInfo(xid))
            }
        }
    }

    fun stopLocation() {
        fusedClient?.removeLocationUpdates(locationCallback)
    }

    fun setCurrentLocation(location: LatLng) {
        _currentLocation = location
    }

    fun setLocationListener(locationListener: LocationSource.OnLocationChangedListener?) {
        this.locationListener = locationListener
    }
}