package com.whyranoid.presentation.running

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
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
import java.util.*

@AndroidEntryPoint
internal class RunningActivity :
    BaseActivity<ActivityRunningBinding>(R.layout.activity_running),
    OnMapReadyCallback,
    LocationSource {

    private val viewModel: RunningViewModel by viewModels()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Snackbar.make(
                binding.root,
                getString(R.string.running_finish_snackbar_content),
                Snackbar.LENGTH_SHORT
            ).setAction(getString(R.string.running_finish_snackbar_action)) {
                viewModel.onFinishButtonClicked()
            }.show()
        }
    }

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var paths: MutableList<PathOverlay>
    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews(savedInstanceState)
        observeState()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        with(naverMap) {
            locationSource = this@RunningActivity
            maxZoom = MAP_MAX_ZOOM
            minZoom = MAP_MIN_ZOOM
            uiSettings.isCompassEnabled = false
            uiSettings.isZoomControlEnabled = false
            uiSettings.isLocationButtonEnabled = false
            uiSettings.setLogoMargin(0, 0, 0, -10)
            locationOverlay.isVisible = true
            locationOverlay.icon = OverlayImage.fromResource(R.drawable.kong)
            locationOverlay.iconWidth = MAP_ICON_SIZE
            locationOverlay.iconHeight = MAP_ICON_SIZE
        }

        observeStateOnMapReady()
        naverMap.addOnCameraChangeListener { reason, animated ->
            if (reason == CameraUpdate.REASON_GESTURE) {
                viewModel.onTrackingCanceledByGesture()
            }
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
        mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        this.listener = null
    }

    private fun initViews(savedInstanceState: Bundle?) {
        mapView = binding.mapView

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.vm = viewModel
        this.onBackPressedDispatcher.addCallback(this, backPressedCallback)
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

        repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is Event.FinishButtonClick -> handleRunningFinishSuccessState(event.runningFinishData)
                    is Event.RunningFinishFailure -> handleRunningFinishFailureState()
                }
            }
        }
    }

    private fun observeStateOnMapReady() {
        repeatWhenUiStarted {
            viewModel.trackingModeState.collect { trackingMode ->
                when (trackingMode) {
                    TrackingMode.NONE -> {
                        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                    }
                    TrackingMode.NO_FOLLOW -> {
                        viewModel.runningState.value.runningData.lastLocation?.let { location ->
                            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(location)))
                        }
                    }
                    TrackingMode.FOLLOW -> {
                        naverMap.locationTrackingMode = LocationTrackingMode.Follow
                    }
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
        runningState.runningData.lastLocation?.let { location ->
            listener?.onLocationChanged(location) ?: run {
                naverMap.let {
                    val locationOverlay = it.locationOverlay
                    locationOverlay.isVisible = true
                    locationOverlay.position = LatLng(location)
                    locationOverlay.bearing = (locationOverlay.bearing + 30) % 360
                    locationOverlay.circleRadius = 100
                }
            }
        }
    }

    private fun updatePathsOverlay(runningState: RunningState) {
        runningState.runningData.runningPositionList.let { runningPositionList ->
            for (index in 0 until paths.size.coerceAtMost(runningPositionList.size)) {
                if (runningState.runningData.runningPositionList[index].size >= 2) {
                    paths[index].coords =
                        runningState.runningData.runningPositionList[index].map { it.toLatLng() }
                    paths[index].map = naverMap
                }
            }
        }
    }

    private fun handleRunningFinishSuccessState(runningFinishData: RunningFinishData) {
        setResult(
            RESULT_OK,
            Intent().putExtra(RunningViewModel.RUNNING_FINISH_DATA_KEY, runningFinishData)
        )
        finish()
    }

    private fun handleRunningFinishFailureState() {
        finish()
    }

    companion object {
        const val MAP_MAX_ZOOM = 18.0
        const val MAP_MIN_ZOOM = 10.0
        const val MAP_ICON_SIZE = 50
    }
}
