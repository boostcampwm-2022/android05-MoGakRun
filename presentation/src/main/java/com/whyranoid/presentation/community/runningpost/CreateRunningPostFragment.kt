package com.whyranoid.presentation.community.runningpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCreateRunningPostBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CreateRunningPostFragment :
    BaseFragment<FragmentCreateRunningPostBinding>(R.layout.fragment_create_running_post) {

    private val viewModel: CreateRunningPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.selectedRunningHistory = viewModel.selectedRunningHistory
        setUpMenu()
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
                        Snackbar.make(binding.root, "내용을 1글자 이상 입력하세요", Snackbar.LENGTH_SHORT)
                            .show()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }
}
