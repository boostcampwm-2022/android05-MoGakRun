package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentMyRunBinding
import com.whyranoid.presentation.util.loadImage
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MyRunFragment : BaseFragment<FragmentMyRunBinding>(R.layout.fragment_my_run) {

    private val viewModel by viewModels<MyRunViewModel>()

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(requireContext())
    }

    private val runningHistoryAdapter = MyRunningHistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeInfo()
    }

    private fun initViews() {
        viewModel.getNickName()
        viewModel.getProfileImgUri()
        binding.ivEditNickName.setOnClickListener {
            popUpEditNickNameDialog()
        }

        binding.rvMyRunningHistory.adapter = runningHistoryAdapter
    }

    private fun observeInfo() {
        repeatWhenUiStarted {
            viewModel.nickName.collect { nickName ->
                binding.tvNickName.text = nickName
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImgUri.collect { profileImgUri ->
                binding.ivProfileImage.loadImage(profileImgUri)
            }
        }

        repeatWhenUiStarted {
            viewModel.runningHistoryList.collect { runningHistoryList ->
                runningHistoryAdapter.submitList(runningHistoryList)
            }
        }
    }

    private fun popUpEditNickNameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_nick_name, null)

        if (dialogView.parent != null) {
            builder.show()
        } else {
            builder.setView(dialogView)
                .setPositiveButton(
                    getString(R.string.my_run_edit_nick_name_dialog_positive)
                ) { dialog, _ ->
                    viewModel.updateNickName(dialogView.findViewById<EditText>(R.id.et_change_nick_name).text.toString())
                    dialog.dismiss()
                }
                .setNegativeButton(
                    getString(R.string.my_run_edit_nick_name_dialog_negative)
                ) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }
}
