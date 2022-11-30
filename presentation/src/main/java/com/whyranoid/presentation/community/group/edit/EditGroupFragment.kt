package com.whyranoid.presentation.community.group.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentEditGroupBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class EditGroupFragment :
    BaseFragment<FragmentEditGroupBinding>(R.layout.fragment_edit_group) {

    private val viewModel: EditGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        initViews()
        observeState()
    }

    private fun initViews() {
        binding.viewModel = viewModel
    }

    private fun handleEvent(event: Event) {
        when (event) {
            // TODO : 다이어로그로 그룹을 수정할 수 있도록 변경
            is Event.AddRuleButtonClick -> {
                Snackbar.make(binding.root, "룰 추가 클릭", Snackbar.LENGTH_SHORT).show()
            }
            is Event.EditGroupButtonClick -> {
                if (event.isSuccess) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_edit_group_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_edit_group_fail),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            is Event.DuplicateCheckButtonClick -> {
                if (event.isDuplicatedGroupName) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_duplicated_group_name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_un_duplicated_group_name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.etGroupName.isEnabled = false
                }
            }
            is Event.WarningButtonClick -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.text_warning_create_group),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.isGroupCreateButtonEnable.collect { isEnable ->
                if (isEnable) {
                    binding.topAppBar.menu.setGroupVisible(R.id.ready_to_create, true)
                    binding.topAppBar.menu.setGroupVisible(R.id.not_ready_to_create, false)
                } else {
                    binding.topAppBar.menu.setGroupVisible(R.id.ready_to_create, false)
                    binding.topAppBar.menu.setGroupVisible(R.id.not_ready_to_create, true)
                }
            }
        }
    }

    private fun setupMenu() {
        with(binding.topAppBar) {
            inflateMenu(R.menu.create_group_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.create_group_button -> {
                        viewModel.emitEvent(Event.EditGroupButtonClick())
                        true
                    }
                    R.id.warning_about_create_group_button -> {
                        viewModel.emitEvent(Event.WarningButtonClick)
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
