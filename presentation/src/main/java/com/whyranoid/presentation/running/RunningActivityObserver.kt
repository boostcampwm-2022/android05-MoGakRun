package com.whyranoid.presentation.running

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.naver.maps.map.MapView

class RunningActivityObserver(
    private val mapView: MapView,
    private val savedInstanceState: Bundle?
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        mapView.onCreate(savedInstanceState)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        mapView.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mapView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        mapView.onPause()
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        mapView.onStop()
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mapView.onDestroy()
        super.onDestroy(owner)
    }
}
