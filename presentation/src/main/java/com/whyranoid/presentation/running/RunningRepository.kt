package com.whyranoid.presentation.running

import android.location.Location
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunningRepository @Inject constructor() {
    private var _runningState = MutableStateFlow<RunningState>(
        RunningState.NotRunning()
    )
    val runningState get() = _runningState.asStateFlow()

    fun startRunning(startPosition: RunningPosition) {
        if (runningState.value is RunningState.NotRunning) {
            _runningState.value = RunningState.Running(
                RunningData(
                    startTime = System.currentTimeMillis(),
                    runningTime = 0,
                    totalDistance = 0.0,
                    pace = 0.0,
                    runningPositionList = listOf(startPosition)
                )
            )
        }
    }

    fun pauseRunning() = runCatching {
        if (runningState.value is RunningState.Running) {
            _runningState.value = RunningState.Paused(
                runningState.value.runningData
            )
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

    fun setRunningState(runningPosition: RunningPosition) {
        if (_runningState.value is RunningState.Running) {
            val prevRunningData = _runningState.value.runningData
            val newRunningTime = prevRunningData.runningTime + 1
            val distance = FloatArray(1)
            Location.distanceBetween(
                prevRunningData.runningPositionList.last().latitude,
                prevRunningData.runningPositionList.last().longitude,
                runningPosition.latitude,
                runningPosition.longitude,
                distance
            )
            val newTotalDistance = prevRunningData.totalDistance + distance.first()
            val newPace = newTotalDistance / newRunningTime
            val newRunningPositionList = prevRunningData.runningPositionList.plus(runningPosition)

            _runningState.value = RunningState.Running(
                RunningData(
                    startTime = prevRunningData.startTime,
                    runningTime = newRunningTime,
                    totalDistance = newTotalDistance,
                    pace = newPace,
                    runningPositionList = newRunningPositionList
                )
            )
        }
    }
}

// TODO : 모델 파일 분리
data class RunningData(
    val startTime: Long = 0L,
    val runningTime: Int = 0,
    val totalDistance: Double = 0.0,
    val pace: Double = 0.0,
    val runningPositionList: List<RunningPosition> = emptyList()
)

data class RunningFinishData(
    val runningHistory: RunningHistory,
    val runningPositionList: List<RunningPosition>
)

sealed interface RunningState {
    val runningData: RunningData

    data class NotRunning(override val runningData: RunningData = RunningData()) : RunningState

    data class Running(override val runningData: RunningData) : RunningState

    data class Paused(override val runningData: RunningData) : RunningState
}

fun RunningData.toRunningFinishData() =
    RunningFinishData(
        RunningHistory(
            historyId = UUID.randomUUID().toString(),
            startedAt = startTime,
            finishedAt = System.currentTimeMillis(),
            totalRunningTime = runningTime,
            pace = pace,
            totalDistance = totalDistance
        ),
        runningPositionList
    )
