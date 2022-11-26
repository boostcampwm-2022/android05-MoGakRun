package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentSettingBinding
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val viewModel by viewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.emailState.collect { emailState ->
                when (emailState) {
                    is UiState.UnInitialized -> {
                        // 초기화 안됨
                    }
                    is UiState.Loading -> {
                        // 로딩 중
                    }
                    is UiState.Success<String> -> initEmailView(emailState.value)
                    is UiState.Failure -> {
                        // 실패
                    }
                }
            }
        }
    }

    private fun initEmailView(email: String) {
        binding.tvConnectedAccount.text = email
    }
}
