package com.whyranoid.presentation.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentSelectRunningHistoryBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.myrun.MyRunningHistoryAdapter
import com.whyranoid.presentation.util.repeatWhenUiStarted

internal class SelectRunningHistoryFragment :
    BaseFragment<FragmentSelectRunningHistoryBinding>(R.layout.fragment_select_running_history) {

    private val viewModel: SelectRunningHistoryViewModel by viewModels()
    private val runningHistoryAdapter = MyRunningHistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        binding.rvSelectRunningHistory.adapter = runningHistoryAdapter
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.runningHistoryListState.collect { runningHistoryState ->
                when (runningHistoryState) {
                    is UiState.UnInitialized -> {
                        // 초기화
                    }
                    is UiState.Loading -> {
                        // 로딩중
                    }

                    is UiState.Success<List<RunningHistoryUiModel>> -> initRecyclerView(
                        runningHistoryState.value
                    )

                    is UiState.Failure -> {
                        // 실패
                    }
                }
            }
        }
    }

    private fun initRecyclerView(runningHistoryList: List<RunningHistoryUiModel>) {
        runningHistoryAdapter.submitList(runningHistoryList)
    }
}
