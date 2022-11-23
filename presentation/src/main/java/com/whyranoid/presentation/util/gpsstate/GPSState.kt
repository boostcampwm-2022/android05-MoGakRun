package com.whyranoid.presentation.util.gpsstate

import android.content.Context
import android.location.LocationManager

object GPSState {

    fun getGpsState(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
