package com.whyranoid.presentation.community.group.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whyranoid.presentation.databinding.GroupSettingDialogBinding

class GroupSettingDialog(
    private val onEditButtonClickListener: () -> Unit,
    private val onDeleteButtonClickListener: () -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "GroupSettingDialog"
    }

    lateinit var binding: GroupSettingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = GroupSettingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEditGroup.setOnClickListener {
            onEditButtonClickListener.invoke()
            dismiss()
        }

        binding.btnDeleteGroup.setOnClickListener {
            onDeleteButtonClickListener.invoke()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}
