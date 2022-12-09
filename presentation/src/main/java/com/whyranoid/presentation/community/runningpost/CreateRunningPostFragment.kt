package com.whyranoid.presentation.community.runningpost

import android.os.Bundle
import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.compose.TopSnackBar
import com.whyranoid.presentation.databinding.FragmentCreateRunningPostBinding
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.util.networkconnection.NetworkState
import com.whyranoid.presentation.util.makeSnackBar
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CreateRunningPostFragment :
    BaseFragment<FragmentCreateRunningPostBinding>(R.layout.fragment_create_running_post) {

    private val viewModel: CreateRunningPostViewModel by viewModels()

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
        binding.selectedRunningHistory = viewModel.selectedRunningHistory
        binding.executePendingBindings()
        setUpMenu()
        binding.cvNetworkConnection.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    val showSnackBar: NetworkState by viewModel.networkState.collectAsState()
                    TopSnackBar(
                        isVisible =
                        showSnackBar is NetworkState.DisConnection,
                        text =
                        getString(R.string.network_connection_alert)
                    )
                }
            }
        }
    }

    private fun observeState() {
        viewModel.startObservingNetworkState()
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.createPostState.collect { createPostState ->
                when (createPostState) {
                    is UiState.UnInitialized -> {}
                    is UiState.Loading -> {}
                    is UiState.Success<Boolean> -> handleCreatePostStateSuccess(createPostState.value)
                    is UiState.Failure -> {}
                }
            }
        }
    }

    private fun setUpMenu() {
        with(binding.topAppBar) {
            inflateMenu(R.menu.community_create_running_post_menu)

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.createPostButtonEnableState.collect { isEnable ->
                    if (isEnable) {
                        menu.setGroupVisible(R.id.ready_to_create_running_post, true)
                        menu.setGroupVisible(R.id.not_ready_to_create_running_post, false)
                    } else {
                        menu.setGroupVisible(R.id.ready_to_create_running_post, false)
                        menu.setGroupVisible(R.id.not_ready_to_create_running_post, true)
                    }
                }
            }

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.create_running_post_button -> {
                        viewModel.createRunningPost()
                        true
                    }
                    R.id.warning_about_create_running_post_button -> {
                        binding.root.makeSnackBar(getString(R.string.community_warning_running_post))
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun handleCreatePostStateSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            val action =
                CreateRunningPostFragmentDirections.actionCreateRunningPostFragmentToCommunityFragment()

            requireView().makeSnackBar(getString(R.string.community_success_create_running_post))
            findNavController().navigate(action)
        } else {
            requireView().makeSnackBar(getString(R.string.community_fail_create_running_post))
        }
    }
}
