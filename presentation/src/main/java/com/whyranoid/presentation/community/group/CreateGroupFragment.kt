package com.whyranoid.presentation.community.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCreateGroupBinding

internal class CreateGroupFragment :
    BaseFragment<FragmentCreateGroupBinding>(R.layout.fragment_create_group) {

    private val viewModel: CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }
}
