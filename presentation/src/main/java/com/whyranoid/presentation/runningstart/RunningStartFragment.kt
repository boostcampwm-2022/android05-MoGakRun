package com.whyranoid.presentation.runningstart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentRunningStartBinding
import com.whyranoid.presentation.running.RunningActivity
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RunningStartFragment :
    BaseFragment<FragmentRunningStartBinding>(R.layout.fragment_running_start) {

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
                startActivity(Intent(requireActivity(), RunningActivity::class.java))
            }
        }
    }
}
