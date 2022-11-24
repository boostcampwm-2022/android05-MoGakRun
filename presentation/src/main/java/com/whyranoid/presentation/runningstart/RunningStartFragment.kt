package com.whyranoid.presentation.runningstart

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentRunningStartBinding
import com.whyranoid.presentation.running.RunningActivity
import com.whyranoid.presentation.util.gpsstate.GPSState
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RunningStartFragment :
    BaseFragment<FragmentRunningStartBinding>(R.layout.fragment_running_start) {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startActivity(Intent(requireContext(), RunningActivity::class.java))
        } else {
            showPermissionRequestDialog()
        }
    }

    private val viewModel by viewModels<RunningStartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        repeatWhenUiStarted {
            viewModel.runnerCount.collect { runnerCount ->
                binding.tvRunnerCountNumber.text = runnerCount.toString()
            }
        }

        repeatWhenUiStarted {
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
                    Snackbar.make(binding.root, "gps 켜주세요", Snackbar.LENGTH_SHORT).show()
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
                startActivity(Intent(requireContext(), RunningActivity::class.java))
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
}
