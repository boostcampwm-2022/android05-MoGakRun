package com.whyranoid.presentation.running

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.whyranoid.domain.usecase.FinishRunningUseCase
import com.whyranoid.domain.usecase.StartRunningUseCase
import com.whyranoid.presentation.running.runningdatamanager.RunningDataManager
import com.whyranoid.presentation.running.runningdatamanager.RunningState
import com.whyranoid.presentation.running.runningdatamanager.toRunningHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startRunningUseCase: StartRunningUseCase,
    private val finishRunningUseCase: FinishRunningUseCase
) : ViewModel() {

    private val runningDataManager = RunningDataManager.getInstance()
    val runningState = runningDataManager.runningState

    private val _trackingModeState = MutableStateFlow(TrackingMode.FOLLOW)
    val trackingModeState get() = _trackingModeState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        if (runningDataManager.runningState.value is RunningState.NotRunning) {
            viewModelScope.launch {
                startRunningUseCase()
            }
        }
        startRunningWorker(context)
    }

    private fun startRunningWorker(context: Context): LiveData<WorkInfo> {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val runningRequest = OneTimeWorkRequestBuilder<RunningWorker>()
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager
            .beginUniqueWork(
                RunningWorker.WORKER_NAME,
                ExistingWorkPolicy.KEEP,
                runningRequest
            )
            .enqueue()

        return workManager.getWorkInfoByIdLiveData(runningRequest.id)
    }

    fun onCheckingPauseOrResume() {
        when (runningDataManager.runningState.value) {
            is RunningState.Running -> onPauseButtonClicked()
            is RunningState.Paused -> onResumeButtonClicked()
            else -> return
        }
    }

    fun onTrackingButtonClicked() {
        when (trackingModeState.value) {
            TrackingMode.NONE -> {
                _trackingModeState.value = TrackingMode.NO_FOLLOW
            }
            TrackingMode.NO_FOLLOW -> {
                _trackingModeState.value = TrackingMode.FOLLOW
            }
            TrackingMode.FOLLOW -> {
                _trackingModeState.value = TrackingMode.NONE
            }
        }
    }

    fun onTrackingCanceledByGesture() {
        _trackingModeState.value = TrackingMode.NONE
    }

    private fun onPauseButtonClicked() {
        runningDataManager.pauseRunning()
    }

    private fun onResumeButtonClicked() {
        runningDataManager.resumeRunning()
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun onFinishButtonClicked() {
        runningDataManager.finishRunning().onSuccess { runningFinishData ->
            viewModelScope.launch {
                finishRunningUseCase(runningFinishData.runningHistory.toRunningHistory())
            }
            emitEvent(Event.FinishButtonClick(runningFinishData))
        }.onFailure {
            emitEvent(Event.RunningFinishFailure)
        }
    }

    companion object {
        const val RUNNING_FINISH_DATA_KEY = "runningFinishData"
    }
}
