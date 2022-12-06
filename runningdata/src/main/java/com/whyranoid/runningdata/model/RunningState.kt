package com.whyranoid.runningdata.model

sealed interface RunningState {
    val runningData: RunningData

    data class NotRunning(override val runningData: RunningData = RunningData()) : RunningState

    data class Running(override val runningData: RunningData) : RunningState

    data class Paused(override val runningData: RunningData) : RunningState
}
