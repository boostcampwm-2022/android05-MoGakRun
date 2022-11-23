package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentMyRunBinding
import com.whyranoid.presentation.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MyRunFragment : BaseFragment<FragmentMyRunBinding>(R.layout.fragment_my_run) {

    private val viewModel by viewModels<MyRunViewModel>()

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
    }

    private fun observeInfo() {
        viewModel.nickName.observe(viewLifecycleOwner) {
            binding.tvNickName.text = it
        }

        viewModel.profileImgUri.observe(viewLifecycleOwner) {
            binding.ivProfileImage.loadImage(it)
        }
    }

    private fun popUpEditNickNameDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setView(R.layout.dialog_edit_nick_name)
            .setPositiveButton(
                getString(R.string.my_run_edit_nick_name_dialog_positive)
            ) { dialog, id ->
                // 닉네임 변경 로직
            }
            .setNegativeButton(
                getString(R.string.my_run_edit_nick_name_dialog_negative)
            ) { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }
}
