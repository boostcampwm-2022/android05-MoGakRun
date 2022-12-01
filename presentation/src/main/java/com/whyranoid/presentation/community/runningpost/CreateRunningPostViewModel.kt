package com.whyranoid.presentation.community.runningpost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.usecase.CreateRunningPostUseCase
import com.whyranoid.presentation.model.RunningHistoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRunningPostViewModel @Inject constructor(
    private val createRunningPostUseCase: CreateRunningPostUseCase,
    private val savedState: SavedStateHandle
) : ViewModel() {

    val selectedRunningHistory =
        savedState.get<RunningHistoryUiModel>(RUNNING_HISTORY_SAFE_ARGS_KEY)
    val runningPostContent = MutableStateFlow<String?>(null)

    val createPostButtonEnableState: StateFlow<Boolean>
        get() = runningPostContent.map { content ->
            content != null
        }.stateIn(
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000),
            scope = viewModelScope
        )

    fun createRunningPost() {
        viewModelScope.launch {
            selectedRunningHistory?.let { runningHistory ->
                createRunningPostUseCase(
                    runningPostContent.value.toString(),
                    runningHistory.historyId
                )
            }
        }
    }

    companion object {
        private const val RUNNING_HISTORY_SAFE_ARGS_KEY = "selectedRunningHistory"
    }

    fun RunningHistoryUiModel.toRunningHistory(): RunningHistory {
        return RunningHistory(
            historyId = historyId,
            startedAt = startedAt.toLong(),
            finishedAt = finishedAt.toLong(),
            totalRunningTime = totalRunningTime.toInt(),
            pace = pace.toDouble(),
            totalDistance = totalDistance.toDouble()
        )
    }
}
