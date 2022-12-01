package com.whyranoid.presentation.community.runningpost

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.presentation.R
import com.whyranoid.presentation.databinding.ItemRunningHistoryBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.myrun.MyRunningHistoryDiffCallback

class CommunityRunningHistoryAdapter(private val selectRunningHistoryListener: RunningHistoryItemListener) :
    ListAdapter<RunningHistoryUiModel, CommunityRunningHistoryViewHolder>(
        MyRunningHistoryDiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityRunningHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_running_history, parent, false)
        return CommunityRunningHistoryViewHolder(view, selectRunningHistoryListener)
    }

    override fun onBindViewHolder(holder: CommunityRunningHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CommunityRunningHistoryViewHolder(
    view: View,
    private val listener: RunningHistoryItemListener
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRunningHistoryBinding.bind(view)

    fun bind(runningHistory: RunningHistoryUiModel) {
        // 아이템이 선택된 개체인지 확인

        binding.runningHistory = runningHistory

        binding.root.setBackgroundColor(Color.TRANSPARENT)

        binding.root.setOnClickListener {
            val isSelected = listener.checkRunningHistoryId(runningHistory)

            // 이미 선택되어 있던 아이템이면
            if (isSelected) {
                listener.unSelectRunningHistory()
                it.setBackgroundColor(Color.TRANSPARENT)
            } else {
                // 선택되어 있지 않던 아이템이면
                listener.selectRunningHistory(runningHistory)
                it.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.gray
                    )
                )
            }
        }
    }
}
