package com.whyranoid.presentation.runningstart

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentRunningStartBinding
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.running.RunningActivity
import com.whyranoid.presentation.running.RunningViewModel.Companion.RUNNING_FINISH_DATA_KEY
import com.whyranoid.presentation.util.getSerializableData
import com.whyranoid.presentation.util.gpsstate.GPSState
import com.whyranoid.presentation.util.makeSnackBar
import com.whyranoid.presentation.util.repeatWhenUiStarted
import com.whyranoid.runningdata.model.RunningFinishData
import com.whyranoid.runningdata.model.RunningState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RunningStartFragment :
    BaseFragment<FragmentRunningStartBinding>(R.layout.fragment_running_start) {

    // 위치 권한 요청
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            runningActivityLauncher.launch(Intent(requireContext(), RunningActivity::class.java))
        } else {
            showPermissionRequestDialog()
        }
    }

    // Running Activity 로 이동 런쳐(운동 결과를 받고 종료 화면으로 이동)
    private val runningActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {

            // 결과 받아오기
            val runningFinishData =
                activityResult.data?.getSerializableData<RunningFinishData>(
                    RUNNING_FINISH_DATA_KEY
                )

            navigateToRunningFinish(runningFinishData)
        }
    }

    private val viewModel by viewModels<RunningStartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    override fun onDestroyView() {
        viewModel.finishObservingNetworkState()
        super.onDestroyView()
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.executePendingBindings()

        if ((viewModel.runningDataManager.runningState.value is RunningState.NotRunning).not()) {
            runningActivityLauncher.launch(
                Intent(
                    requireContext(),
                    RunningActivity::class.java
                )
            )
        }
    }

    private fun observeState() {
        viewModel.startObservingNetworkState()
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.runnerCountState.collect { runnerCountState ->
                when (runnerCountState) {
                    is UiState.Failure -> {}
                    is UiState.Loading -> {}
                    is UiState.Success -> binding.tvRunnerCountNumber.text = runnerCountState.value.toString()
                    is UiState.UnInitialized -> {}
                }
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.RunningStartButtonClick -> {
                if (GPSState.getGpsState(requireContext())) {
                    tryRunningStart()
                } else {
                    binding.root.makeSnackBar(getString(R.string.running_start_turn_on_gps)).show()
                }
            }
        }
    }

    private fun tryRunningStart() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                runningActivityLauncher.launch(
                    Intent(
                        requireContext(),
                        RunningActivity::class.java
                    )
                )
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // 다이얼로그 띄우기, 거부된 상태
                showPermissionRequestDialog()
            }
            else -> {
                // 최초 요청
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun showPermissionRequestDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("위치 권한 요청")
            .setMessage("모각런에서 달리기 위해서는 GPS 사용 권한이 필요해요")
            .setPositiveButton("허용할게요") { _, _ ->
                startActivity(
                    Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context?.packageName ?: "", null)
                    )
                )
            }
            .setNegativeButton("그건 싫어요") { _, _ ->
            }
            .show()
    }

    private fun navigateToRunningFinish(runningFinishData: RunningFinishData?) {
        runningFinishData?.let { it ->
            if (it.runningPositionList.flatten().isNotEmpty()) {
                val direction =
                    RunningStartFragmentDirections.actionRunningStartFragmentToRunningFinish(
                        it
                    )
                findNavController().navigate(direction)
            }
        } ?: binding.root.makeSnackBar(getString(R.string.running_start_error_message)).show()
    }
}
