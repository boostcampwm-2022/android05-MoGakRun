package com.whyranoid.presentation.running

import com.naver.maps.geometry.LatLng

data class RunningPosition(
    val latitude: Double,
    val longitude: Double
)

fun RunningPosition.toLatLng(): LatLng {
    return LatLng(
        this.latitude,
        this.longitude
    )
}
