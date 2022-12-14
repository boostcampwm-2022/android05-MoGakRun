package com.whyranoid.presentation.myrun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.presentation.R
import com.whyranoid.presentation.databinding.ItemRunningHistoryBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel

class MyRunningHistoryAdapter :
    ListAdapter<RunningHistoryUiModel, MyRunningHistoryViewHolder>(
        MyRunningHistoryDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRunningHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_running_history, parent, false)
        return MyRunningHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRunningHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MyRunningHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRunningHistoryBinding.bind(view)

    fun bind(runningHistory: RunningHistoryUiModel) {
        binding.runningHistory = runningHistory
    }
}

class MyRunningHistoryDiffCallback : DiffUtil.ItemCallback<RunningHistoryUiModel>() {
    override fun areItemsTheSame(
        oldItem: RunningHistoryUiModel,
        newItem: RunningHistoryUiModel
    ): Boolean {
        return oldItem.historyId == newItem.historyId
    }

    override fun areContentsTheSame(
        oldItem: RunningHistoryUiModel,
        newItem: RunningHistoryUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
