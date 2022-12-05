package com.whyranoid.presentation.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.presentation.databinding.MyGroupItemBinding
import com.whyranoid.presentation.model.GroupInfoUiModel

class MyGroupAdapter(private val onClickListener: (GroupInfoUiModel) -> Unit) :
    ListAdapter<GroupInfoUiModel, MyGroupAdapter.MyGroupViewHolder>(diffUtil) {

    class MyGroupViewHolder(private val binding: MyGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(groupInfo: GroupInfoUiModel) {
            binding.groupInfo = groupInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        val layoutInflater =
            MyGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyGroupViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.apply {
            itemView.setOnClickListener {
                curItem?.let { groupInfo ->
                    onClickListener(groupInfo)
                }
            }
            bind(groupInfo = curItem)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GroupInfoUiModel>() {
            override fun areItemsTheSame(
                oldItem: GroupInfoUiModel,
                newItem: GroupInfoUiModel
            ): Boolean =
                oldItem.groupId == newItem.groupId

            override fun areContentsTheSame(
                oldItem: GroupInfoUiModel,
                newItem: GroupInfoUiModel
            ): Boolean =
                oldItem == newItem
        }
    }
}
