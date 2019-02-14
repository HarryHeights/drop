package mcneil.peter.drop.util

import android.annotation.SuppressLint
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import mcneil.peter.drop.activities.FINE_LOCATION
import pub.devrel.easypermissions.AfterPermissionGranted

class LocationUtil(private val locationManager: LocationManager) {

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(FINE_LOCATION)
    fun currentLocation(): Location {
        val provider: String? = locationManager.getBestProvider(Criteria(), true)
        return locationManager.getLastKnownLocation(provider ?: LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(FINE_LOCATION)
    fun onUpdate(locationListener: LocationListener) {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

}