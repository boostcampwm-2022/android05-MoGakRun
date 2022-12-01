package com.whyranoid.presentation.community.runningpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentSelectRunningHistoryBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SelectRunningHistoryFragment :
    BaseFragment<FragmentSelectRunningHistoryBinding>(R.layout.fragment_select_running_history),
    RunningHistoryItemListener {

    private val viewModel: SelectRunningHistoryViewModel by viewModels()
    private val runningHistoryAdapter = CommunityRunningHistoryAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        binding.rvSelectRunningHistory.adapter = runningHistoryAdapter
        setupMenu()
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

                    is UiState.Success<List<RunningHistoryUiModel>> -> handleRunningHistoryListSuccessState(
                        runningHistoryState.value
                    )

                    is UiState.Failure -> {
                        // 실패
                    }
                }
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.selectedRunningHistory.collect { selectedRunningHistory ->
                if (selectedRunningHistory != null) {
                    binding.topAppBar.menu.setGroupVisible(R.id.ready_to_create_running_post, true)
                    binding.topAppBar.menu.setGroupVisible(
                        R.id.not_ready_to_create_running_post,
                        false
                    )
                } else {
                    binding.topAppBar.menu.setGroupVisible(R.id.ready_to_create_running_post, false)
                    binding.topAppBar.menu.setGroupVisible(
                        R.id.not_ready_to_create_running_post,
                        true
                    )
                }
            }
        }
    }

    private fun handleRunningHistoryListSuccessState(runningHistoryList: List<RunningHistoryUiModel>) {
        runningHistoryAdapter.submitList(runningHistoryList)
    }

    private fun setupMenu() {
        with(binding.topAppBar) {
            inflateMenu(R.menu.community_select_running_history_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.running_history_selected_button -> {
                        val action = viewModel.getSelectedRunningHistory()?.let { runningHistory ->
                            SelectRunningHistoryFragmentDirections.actionSelectRunningHistoryFragmentToCreateRunningPostFragment(
                                runningHistory
                            )
                        }
                        if (action != null) {
                            findNavController().navigate(action)
                        }
                        true
                    }
                    R.id.warning_select_running_history_button -> {
                        Snackbar.make(binding.root, getString(R.string.community_select_running_history_snack_bar), Snackbar.LENGTH_SHORT).show()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun checkRunningHistoryId(runningHistory: RunningHistoryUiModel): Boolean =
        viewModel.getSelectedRunningHistory()?.historyId == runningHistory.historyId

    override fun selectRunningHistory(runningHistory: RunningHistoryUiModel) {
        viewModel.setSelectedRunningHistory(runningHistory)
    }

    override fun unSelectRunningHistory() {
        viewModel.setSelectedRunningHistory(null)
    }
}
