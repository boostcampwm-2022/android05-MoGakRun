package com.whyranoid.presentation.myrun

import androidx.recyclerview.widget.DiffUtil
import com.whyranoid.domain.model.RunningHistory

class MyRunningHistoryDiffCallback : DiffUtil.ItemCallback<RunningHistory>() {
    override fun areItemsTheSame(oldItem: RunningHistory, newItem: RunningHistory): Boolean {
        return oldItem.historyId == newItem.historyId
    }

    override fun areContentsTheSame(oldItem: RunningHistory, newItem: RunningHistory): Boolean {
        return oldItem == newItem
    }
}
