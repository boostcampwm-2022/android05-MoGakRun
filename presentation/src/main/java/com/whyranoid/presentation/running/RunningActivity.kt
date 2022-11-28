package com.whyranoid.presentation.running

import android.os.Bundle
import androidx.activity.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseActivity
import com.whyranoid.presentation.databinding.ActivityRunningBinding
import com.whyranoid.presentation.util.dateToString
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
internal class RunningActivity :
    BaseActivity<ActivityRunningBinding>(R.layout.activity_running), OnMapReadyCallback {

    private val viewModel: RunningViewModel by viewModels()

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = binding.mapView

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.vm = viewModel

        repeatWhenUiStarted {
            viewModel.runningState.collect { runningState ->
                with(runningState.runningData) {
                    binding.tvStartTime.text = Date(startTime).dateToString("hh:mm")
                    binding.tvRunningTime.text =
                        String.format("%d:%02d", runningTime / 60, runningTime % 60)
                    binding.tvTotalDistance.text = String.format("%.4f m", totalDistance)
                    binding.tvPace.text = String.format("%.4f km/h", pace * 3.6)
                }
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        naverMap.moveCamera(CameraUpdate.scrollAndZoomTo(LatLng(37.498095, 127.027610), 15.0))
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
