package com.whyranoid.presentation.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.presentation.databinding.MyGroupItemBinding

class MyGroupAdapter(private val onClickListener: (GroupInfo) -> Unit) :
    ListAdapter<GroupInfo, MyGroupAdapter.MyGroupViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GroupInfo>() {
            override fun areItemsTheSame(oldItem: GroupInfo, newItem: GroupInfo): Boolean =
                oldItem.groupId == newItem.groupId

            override fun areContentsTheSame(oldItem: GroupInfo, newItem: GroupInfo): Boolean =
                oldItem == newItem
        }
    }

    class MyGroupViewHolder(private val binding: MyGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(groupInfo: GroupInfo) {
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
}
