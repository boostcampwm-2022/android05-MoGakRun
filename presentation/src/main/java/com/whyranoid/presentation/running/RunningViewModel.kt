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
import com.whyranoid.domain.usecase.StartRunningUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startRunningUseCase: StartRunningUseCase,
    private val runningRepository: RunningRepository
) : ViewModel() {

    val runningState = runningRepository.runningState

    init {
        if (runningRepository.runningState.value is RunningState.NotRunning) {
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
        when (runningRepository.runningState.value) {
            is RunningState.Running -> onPauseButtonClicked()
            is RunningState.Paused -> onResumeButtonClicked()
            else -> return
        }
    }

    private fun onPauseButtonClicked() {
        runningRepository.pauseRunning()
    }

    private fun onResumeButtonClicked() {
        runningRepository.resumeRunning()
    }

    fun onFinishButtonClicked() {
        // TODO: 액티비티에 이벤트 알려주기
        runningRepository.finishRunning()
    }
}
