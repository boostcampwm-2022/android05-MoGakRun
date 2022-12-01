package com.whyranoid.presentation.community.runningpost

import com.whyranoid.presentation.model.RunningHistoryUiModel

interface RunningHistoryItemListener {
    fun checkRunningHistoryId(runningHistory: RunningHistoryUiModel): Boolean
    fun selectRunningHistory(runningHistory: RunningHistoryUiModel)
    fun unSelectRunningHistory()
}
