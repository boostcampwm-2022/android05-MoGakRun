package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentMyRunBinding
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
    }

    private fun observeInfo() {
        viewModel.nickName.observe(viewLifecycleOwner) {
            binding.tvNickName.text = it
        }
    }
}
