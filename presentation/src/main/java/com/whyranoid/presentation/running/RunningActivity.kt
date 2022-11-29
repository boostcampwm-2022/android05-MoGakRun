package com.whyranoid.presentation.running

import android.os.Bundle
import androidx.activity.viewModels
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
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
    private lateinit var paths: MutableList<PathOverlay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews(savedInstanceState)
        observeState()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        with(naverMap) {
            maxZoom = MAP_MAX_ZOOM
            minZoom = MAP_MIN_ZOOM
            uiSettings.isLocationButtonEnabled = false
            uiSettings.isZoomControlEnabled = false
            locationOverlay.isVisible = true
            locationOverlay.icon = OverlayImage.fromResource(R.drawable.kong)
            locationOverlay.iconWidth = MAP_ICON_SIZE
            locationOverlay.iconHeight = MAP_ICON_SIZE
        }

        paths = mutableListOf()

        repeatWhenUiStarted {
            viewModel.runningState.collect { runningState ->
                makeNewPath(runningState)
                updateRunnerPosition(runningState)
                updatePathsOverlay(runningState)
            }
        }
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

    private fun initViews(savedInstanceState: Bundle?) {
        mapView = binding.mapView

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.vm = viewModel
    }

    private fun observeState() {
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

    private fun provideMogakrunPath(): PathOverlay {
        return PathOverlay().apply {
            color = getColor(R.color.mogakrun_secondary_light)
            width = 20
        }
    }

    private fun makeNewPath(runningState: RunningState) {
        while (paths.size < runningState.runningData.runningPositionList.size) {
            paths.add(provideMogakrunPath())
        }
    }

    private fun updateRunnerPosition(runningState: RunningState) {
        runningState.runningData.runningPositionList.last().lastOrNull()?.let {
            naverMap.locationOverlay.position = it.toLatLng()
        }
    }

    private fun updatePathsOverlay(runningState: RunningState) {
        if (runningState.runningData.runningPositionList.last().size >= 2) {
            paths.last().coords =
                runningState.runningData.runningPositionList.last().map { it.toLatLng() }
            paths.last().map = naverMap
        }
    }

    companion object {
        const val MAP_MAX_ZOOM = 18.0
        const val MAP_MIN_ZOOM = 10.0
        const val MAP_ICON_SIZE = 80
    }
}
