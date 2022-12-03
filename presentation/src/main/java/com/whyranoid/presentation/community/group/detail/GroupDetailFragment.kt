package com.whyranoid.presentation.community.group.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentGroupDetailBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class GroupDetailFragment :
    BaseFragment<FragmentGroupDetailBinding>(R.layout.fragment_group_detail) {

    private val viewModel: GroupDetailViewModel by viewModels()
    private val groupDetailArgs by navArgs<GroupDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        handleEvent()
        setBindingData()
        setNotificationAdapter()
    }

    private fun setupMenu() {
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding.topAppBar) {
                val myUid = viewModel.getUidUseCase()

                if (viewModel.leaderId == myUid) {
                    inflateMenu(R.menu.group_detail_menu)
                    binding.btnRecruit.visibility = View.VISIBLE
                } else {
                    binding.btnExitGroup.visibility = View.VISIBLE
                }

                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.setting_group -> {
                            // TODO : BottomSheetDialog Material Theme 적용
                            val dialog = GroupSettingDialog(
                                // TODO : 그룹 수정으로 이동
                                onEditButtonClickListener = {
                                    val action =
                                        GroupDetailFragmentDirections.actionGroupDetailFragmentToEditGroupFragment(
                                            groupDetailArgs.groupInfo
                                        )
                                    findNavController().navigate(action)
                                },
                                // TODO : 그룹 삭제
                                onDeleteButtonClickListener = {
                                    Toast.makeText(context, "그룹 삭제하기", Toast.LENGTH_SHORT).show()
                                }
                            )

                            dialog.show(
                                requireActivity().supportFragmentManager,
                                GroupSettingDialog.TAG
                            )

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

    private fun handleEvent() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    Event.RecruitButtonClick -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.text_check_recruit),
                            Snackbar.LENGTH_SHORT
                        ).setAction(R.string.text_recruit) {
                            viewModel.onRecruitSnackBarButtonClick()
                        }.show()
                    }
                    is Event.RecruitSnackBarButtonClick -> {
                        if (event.isSuccess) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.text_recruit_success),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.text_recruit_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Event.ExitGroupButtonClick -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.text_check_exit_group),
                            Snackbar.LENGTH_SHORT
                        ).setAction(getString(R.string.text_exit_group)) {
                            viewModel.onExitGroupSnackBarButtonClick()
                        }.show()
                    }
                    is Event.ExitGroupSnackBarButtonClick -> {
                        if (event.isSuccess) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.text_exit_group_success),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        } else {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.text_exit_group_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setBindingData() {
        binding.viewModel = viewModel
    }

    private fun setNotificationAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            val uid = viewModel.getUidUseCase()
            val notificationAdapter = GroupNotificationAdapter(uid)

            binding.notificationRecyclerView.adapter = notificationAdapter
            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.mergedNotifications.collect { notifications ->
                    notificationAdapter.submitList(notifications)
                }
            }
        }
    }
}
