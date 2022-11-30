package com.whyranoid.presentation.runningfinish

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentRunningFinishBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.running.RunningFinishData
import com.whyranoid.presentation.running.toLatLng
import com.whyranoid.presentation.util.pxdp.PxDpUtil
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class RunningFinishFragment :
    BaseFragment<FragmentRunningFinishBinding>(R.layout.fragment_running_finish),
    OnMapReadyCallback {

    private val viewModel: RunningFinishViewModel by viewModels()

    private var naverMap: NaverMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        binding.vm = viewModel
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.runningFinishDataState.collect { state ->
                when (state) {
                    is UiState.UnInitialized -> handleDataStateUninitialized()
                    is UiState.Loading -> handleDataStateLoading()
                    is UiState.Success -> handleDataStateSuccess(state.value)
                    is UiState.Failure -> handleDataStateFailure(state.throwable)
                }
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleDataStateUninitialized() {
        Timber.d("running finish Uninitialized")
    }

    private fun handleDataStateLoading() {
        Timber.d("running finish Loading")
    }

    private fun handleDataStateSuccess(runningFinishData: RunningFinishData) {
        binding.runningHistoryItem.runningHistory = runningFinishData.runningHistory

        naverMap?.let {
            val cameraUpdate = CameraUpdate.fitBounds(
                LatLngBounds.Builder().include(
                    runningFinishData.runningPositionList.flatten().map { position ->
                        position.toLatLng()
                    }
                ).build(),
                PxDpUtil.pxToDp(requireContext(), 20)
            )
            it.moveCamera(cameraUpdate)
        }
    }

    private fun handleDataStateFailure(throwable: Throwable) {
        Timber.d(throwable.message)
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.NegativeButtonButtonClick -> findNavController().popBackStack()
            is Event.PositiveButtonClick -> handlePositiveButtonClicked(event.runningHistory)
        }
    }

    private fun handlePositiveButtonClicked(runningHistory: RunningHistoryUiModel) {
        // TODO Safe-Args 활용 CreateRunningPostFragment로 이동
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        if (viewModel.runningFinishDataState.value is UiState.Success) {
            handleDataStateSuccess(
                (viewModel.runningFinishDataState.value as UiState.Success<RunningFinishData>).value
            )
        }
    }
}
