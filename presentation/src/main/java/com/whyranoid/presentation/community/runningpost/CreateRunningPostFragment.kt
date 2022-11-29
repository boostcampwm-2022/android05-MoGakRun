package com.whyranoid.presentation.community.runningpost

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCreateRunningPostBinding

internal class CreateRunningPostFragment :
    BaseFragment<FragmentCreateRunningPostBinding>(R.layout.fragment_create_running_post) {

    private val args: CreateRunningPostFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.selectedRunningHistory = args.selectedRunningHistory
    }
}
