package com.whyranoid.presentation.community.group.create

import android.os.Bundle
import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.compose.RulePicker
import com.whyranoid.presentation.databinding.FragmentCreateGroupBinding
import com.whyranoid.presentation.util.makeSnackBar
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CreateGroupFragment :
    BaseFragment<FragmentCreateGroupBinding>(R.layout.fragment_create_group) {

    private val viewModel: CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        binding.viewModel = viewModel
        setupMenu()
    }

    private fun observeState() {
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

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.CreateGroupButtonClick -> {
                if (event.isSuccess) {
                    binding.root.makeSnackBar(getString(R.string.text_create_group_success)).show()
                    findNavController().popBackStack()
                } else {
                    binding.root.makeSnackBar(getString(R.string.text_create_group_fail)).show()
                }
            }
            is Event.DuplicateCheckButtonClick -> {
                if (event.isDuplicatedGroupName) {
                    binding.root.makeSnackBar(getString(R.string.text_duplicated_group_name)).show()
                } else {
                    binding.root.makeSnackBar(getString(R.string.text_un_duplicated_group_name)).show()
                    binding.etGroupName.isEnabled = false
                }
            }
            is Event.WarningButtonClick -> {
                binding.root.makeSnackBar(getString(R.string.text_warning_create_group)).show()
            }
            is Event.AddRuleButtonClick -> {
                binding.composeView.apply {
                    setViewCompositionStrategy(
                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                    )
                    viewModel.onOpenDialogClicked()
                    setContent {
                        val showDialogState: Boolean by viewModel.showDialog.collectAsState()
                        MaterialTheme {
                            RulePicker(showDialogState, viewModel)
                        }
                    }
                }
            }
            is Event.InvalidRule -> {
                binding.root.makeSnackBar(getString(R.string.text_invalid_rule)).show()
            }
        }
    }

    private fun setupMenu() {
        with(binding.topAppBar) {
            inflateMenu(R.menu.create_group_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.create_group_button -> {
                        viewModel.emitEvent(Event.CreateGroupButtonClick())
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
