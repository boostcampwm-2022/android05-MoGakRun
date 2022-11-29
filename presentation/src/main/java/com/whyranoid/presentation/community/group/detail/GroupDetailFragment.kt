package com.whyranoid.presentation.community.group.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentGroupDetailBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

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
        with(binding.topAppBar) {
            // TODO : uid를 DataStore에서 가져올 수 있도록 변경
            if (viewModel.isLeader) inflateMenu(R.menu.group_detail_menu)

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

    private fun handleEvent() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    // TODO : 홍보 글 쓰러가기
                    Event.RecruitButtonClick -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.text_recruit),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    // TODO : 그룹 나가기
                    Event.ExitGroupButtonClick -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.text_exit_group),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setBindingData() {
        binding.viewModel = viewModel
    }

    // TODO : uid를 DataStore에서 가져올 수 있도록 변경
    private fun setNotificationAdapter() {
        val notificationAdapter = GroupNotificationAdapter("hsjeon")

        binding.notificationRecyclerView.adapter = notificationAdapter
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.mergedNotifications.collect { notifications ->
                notificationAdapter.submitList(notifications)
            }
        }
    }
}
