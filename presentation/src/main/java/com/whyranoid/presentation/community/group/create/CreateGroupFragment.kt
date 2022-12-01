package com.whyranoid.presentation.community.group.create

import android.os.Bundle
import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.compose.RulePicker
import com.whyranoid.presentation.databinding.FragmentCreateGroupBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CreateGroupFragment :
    BaseFragment<FragmentCreateGroupBinding>(R.layout.fragment_create_group) {

    private val viewModel: CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setupMenu()

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.CreateGroupButtonClick -> {
                if (event.isSuccess) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_create_group_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_create_group_fail),
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
}
