package com.whyranoid.presentation.running.runningdatamanager

import android.location.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class RunningDataManager {
    private var _runningState = MutableStateFlow<RunningState>(
        RunningState.NotRunning()
    )
    val runningState get() = _runningState.asStateFlow()

    fun startRunning() {
        if (runningState.value is RunningState.NotRunning) {
            _runningState.value = RunningState.Running(
                RunningData(
                    startTime = System.currentTimeMillis(),
                    runningTime = 0,
                    totalDistance = 0.0,
                    pace = 0.0,
                    runningPositionList = listOf(emptyList())
                )
            )
        }
    }

    fun pauseRunning() = runCatching {
        if (runningState.value is RunningState.Running) {
            val newRunningData = runningState.value.runningData.copy(
                runningPositionList = runningState.value.runningData.runningPositionList.toMutableList()
                    .also { it.add(emptyList()) }
            )
            _runningState.value = RunningState.Paused(newRunningData)
        } else {
            throw Exception("상태가 잘못되었습니다.")
        }
    }

    fun resumeRunning() = runCatching {
        if (runningState.value is RunningState.Paused) {
            _runningState.value = RunningState.Running(
                runningState.value.runningData
            )
        } else {
            throw Exception("상태가 잘못되었습니다.")
        }
    }

    fun finishRunning(): Result<RunningFinishData> = runCatching {
        val finishData = when (runningState.value) {
            is RunningState.NotRunning -> {
                throw Exception("상태가 잘못되었습니다.")
            }
            is RunningState.Running -> {
                runningState.value.runningData.toRunningFinishData()
            }
            is RunningState.Paused -> {
                runningState.value.runningData.toRunningFinishData()
            }
        }
        _runningState.value = RunningState.NotRunning()
        finishData
    }

    fun setRunningState(location: Location) {
        if (_runningState.value is RunningState.Running) {
            val runningPosition = RunningPosition(location.latitude, location.longitude)
            val prevRunningData = _runningState.value.runningData
            val newRunningTime = prevRunningData.runningTime
            val distance = FloatArray(1)

            Location.distanceBetween(
                prevRunningData.runningPositionList.last().lastOrNull()?.latitude
                    ?: runningPosition.latitude,
                prevRunningData.runningPositionList.last().lastOrNull()?.longitude
                    ?: runningPosition.longitude,
                runningPosition.latitude,
                runningPosition.longitude,
                distance
            )
            val newTotalDistance = prevRunningData.totalDistance + distance.first()
            val newPace = if (newRunningTime == 0) 0.0 else newTotalDistance / newRunningTime
            val newRunningPositionList =
                prevRunningData.runningPositionList.toList().map { it.toMutableList() }
                    .also { it[it.lastIndex].add(runningPosition) }

            _runningState.value = RunningState.Running(
                RunningData(
                    startTime = prevRunningData.startTime,
                    runningTime = newRunningTime,
                    totalDistance = newTotalDistance,
                    pace = newPace,
                    runningPositionList = newRunningPositionList,
                    lastLocation = location
                )
            )
        }
    }

    fun tick() {
        _runningState.value = _runningState.value.let {
            it.runningData.copy(runningTime = it.runningData.runningTime + 1)
        }.let { RunningState.Running(it) }
    }

    companion object {
        private var INSTANCE: RunningDataManager? = null

        fun getInstance(): RunningDataManager {
            return INSTANCE ?: RunningDataManager().also {
                INSTANCE = it
            }
        }
    }
}

// TODO : 모델 파일 분리
data class RunningData(
    val startTime: Long = 0L,
    val runningTime: Int = 0,
    val totalDistance: Double = 0.0,
    val pace: Double = 0.0,
    val runningPositionList: List<List<RunningPosition>> = listOf(emptyList()),
    val lastLocation: Location? = null
)

data class RunningFinishData(
    val runningHistory: RunningHistoryModel,
    val runningPositionList: List<List<RunningPosition>>
) : java.io.Serializable

sealed interface RunningState {
    val runningData: RunningData

    data class NotRunning(override val runningData: RunningData = RunningData()) : RunningState

    data class Running(override val runningData: RunningData) : RunningState

    data class Paused(override val runningData: RunningData) : RunningState
}

fun RunningData.toRunningFinishData() =
    RunningFinishData(
        RunningHistoryModel(
            historyId = UUID.randomUUID().toString(),
            startedAt = startTime,
            finishedAt = System.currentTimeMillis(),
            totalRunningTime = runningTime,
            pace = pace,
            totalDistance = totalDistance
        ),
        runningPositionList
    )

data class RunningHistoryModel(
    val historyId: String = "",
    val startedAt: Long = 0L,
    val finishedAt: Long = 0L,
    val totalRunningTime: Int = 0,
    val pace: Double = 0.0,
    val totalDistance: Double = 0.0
) : java.io.Serializable
