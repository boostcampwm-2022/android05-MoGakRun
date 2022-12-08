package com.whyranoid.runningdata.model

data class RunningFinishData(
    val runningHistory: RunningHistoryModel,
    val runningPositionList: List<List<RunningPosition>>
) : java.io.Serializable
