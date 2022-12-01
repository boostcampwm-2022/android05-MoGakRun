package com.whyranoid.presentation.runningfinish

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.MoGakRunException
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.running.RunningFinishData
import com.whyranoid.presentation.running.RunningViewModel.Companion.RUNNING_FINISH_DATA_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningFinishViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {

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
            } ?: kotlin.run {
                _runningFinishDataState.value = UiState.Failure(
                    MoGakRunException.FileNotFoundedException
                )
            }
        }
    }
}
