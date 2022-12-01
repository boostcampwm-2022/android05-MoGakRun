package com.whyranoid.presentation.runningfinish

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentRunningFinishBinding
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.running.RunningFinishData
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class RunningFinishFragment :
    BaseFragment<FragmentRunningFinishBinding>(R.layout.fragment_running_finish) {

    private val viewModel: RunningFinishViewModel by viewModels()

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
    }

    private fun handleDataStateUninitialized() {
        Timber.d("running finish Uninitialized")
    }

    private fun handleDataStateLoading() {
        Timber.d("running finish Loading")
    }

    private fun handleDataStateSuccess(runningFinishData: RunningFinishData) {
        Timber.d(runningFinishData.toString())
    }

    private fun handleDataStateFailure(throwable: Throwable) {
        Timber.d(throwable.message)
    }
}
