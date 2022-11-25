package com.whyranoid.presentation.myrun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.presentation.R
import com.whyranoid.presentation.databinding.ItemRunningHistoryBinding
import com.whyranoid.presentation.model.toRunningHistoryUiModel

class MyRunningHistoryAdapter :
    ListAdapter<RunningHistory, RunningHistoryViewHolder>(
        MyRunningHistoryDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_running_history, parent, false)
        return RunningHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunningHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        class MyRunningHistoryDiffCallback : DiffUtil.ItemCallback<RunningHistory>() {
            override fun areItemsTheSame(
                oldItem: RunningHistory,
                newItem: RunningHistory
            ): Boolean {
                return oldItem.historyId == newItem.historyId
            }

            override fun areContentsTheSame(
                oldItem: RunningHistory,
                newItem: RunningHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class RunningHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRunningHistoryBinding.bind(view)

    fun bind(runningHistory: RunningHistory) {
        binding.runningHistory = runningHistory.toRunningHistoryUiModel()
    }
}
