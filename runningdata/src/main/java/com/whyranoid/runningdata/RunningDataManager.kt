package com.whyranoid.runningdata

import android.location.Location
import com.whyranoid.runningdata.model.RunningData
import com.whyranoid.runningdata.model.RunningFinishData
import com.whyranoid.runningdata.model.RunningPosition
import com.whyranoid.runningdata.model.RunningState
import com.whyranoid.runningdata.model.toRunningFinishData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

        @Synchronized
        fun getInstance(): RunningDataManager {
            return INSTANCE ?: RunningDataManager().also {
                INSTANCE = it
            }
        }
    }
}
