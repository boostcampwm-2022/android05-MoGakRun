package com.whyranoid.presentation.runningfinish

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.MoGakRunException
import com.whyranoid.domain.usecase.SaveRunningHistoryUseCase
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.running.RunningViewModel.Companion.RUNNING_FINISH_DATA_KEY
import com.whyranoid.presentation.running.toRunningHistoryUiModel
import com.whyranoid.runningdata.model.RunningFinishData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningFinishViewModel @Inject constructor(
    private val saveRunningHistoryUseCase: SaveRunningHistoryUseCase,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow get() = _eventFlow.asSharedFlow()

    private val _runningFinishDataState =
        MutableStateFlow<UiState<RunningFinishData>>(UiState.UnInitialized)
    val runningFinishDataState get() = _runningFinishDataState.asStateFlow()

    init {
        getRunningFinishDataState()
    }

    private fun getRunningFinishDataState() {
        _runningFinishDataState.value = UiState.Loading
        viewModelScope.launch {
            stateHandle.get<RunningFinishData>(RUNNING_FINISH_DATA_KEY)?.let { runningFinishData ->
                _runningFinishDataState.value = UiState.Success(runningFinishData)
                with(runningFinishData.runningHistory) {
                    saveRunningHistoryUseCase(
                        historyId,
                        startedAt,
                        finishedAt,
                        totalRunningTime,
                        pace,
                        totalDistance
                    )
                }
            } ?: kotlin.run {
                _runningFinishDataState.value = UiState.Failure(
                    MoGakRunException.FileNotFoundedException
                )
            }
        }
    }

    fun onPositiveButtonClicked() {
        (runningFinishDataState.value as? UiState.Success<RunningFinishData>)?.value?.let { runningFinishData ->
            emitEvent(Event.PositiveButtonClick(runningFinishData.runningHistory.toRunningHistoryUiModel()))
        } ?: kotlin.run {
            _runningFinishDataState.value = UiState.Failure(
                MoGakRunException.FileNotFoundedException
            )
        }
    }

    fun onNegativeButtonClicked() {
        emitEvent(Event.NegativeButtonButtonClick)
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
