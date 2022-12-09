package com.whyranoid.presentation.community.group.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentEditGroupBinding
import com.whyranoid.presentation.util.makeSnackBar
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class EditGroupFragment :
    BaseFragment<FragmentEditGroupBinding>(R.layout.fragment_edit_group) {

    private val viewModel: EditGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    private fun initViews() {
        binding.viewModel = viewModel
        setupMenu()
    }

    private fun handleEvent(event: Event) {
        when (event) {
            // TODO : 다이어로그로 그룹을 수정할 수 있도록 변경
            is Event.AddRuleButtonClick -> {
                binding.root.makeSnackBar(getString(R.string.community_click_add_rule)).show()
            }
            is Event.EditGroupButtonClick -> {
                if (event.isSuccess) {
                    binding.root.makeSnackBar(getString(R.string.text_edit_group_success)).show()
                    findNavController().popBackStack()
                } else {
                    binding.root.makeSnackBar(getString(R.string.text_edit_group_fail)).show()
                }
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
            viewModel.rules.collect { rules ->
                binding.ruleChipGroup.apply {
                    removeAllViews()
                    rules.forEach { rule ->
                        addView(
                            Chip(requireContext()).apply {
                                isCloseIconVisible = true
                                setChipBackgroundColorResource(R.color.mogakrun_primary)
                                text = rule
                                setOnCloseIconClickListener {
                                    viewModel.removeRule(rule)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setupMenu() {
        with(binding.topAppBar) {
            inflateMenu(R.menu.create_group_menu)
            menu.setGroupVisible(R.id.ready_to_create, true)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.create_group_button -> {
                        viewModel.emitEvent(Event.EditGroupButtonClick())
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
