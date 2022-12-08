package com.whyranoid.presentation.runningstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetRunnerCountUseCase
import com.whyranoid.presentation.util.networkconnection.NetworkConnectionStateHolder
import com.whyranoid.presentation.util.networkconnection.NetworkState
import com.whyranoid.runningdata.RunningDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningStartViewModel @Inject constructor(
    getRunnerCountUseCase: GetRunnerCountUseCase,
    private val networkConnectionStateHolder: NetworkConnectionStateHolder
) : ViewModel() {

    val runningDataManager = RunningDataManager.getInstance()

    @OptIn(FlowPreview::class)
    val networkState = networkConnectionStateHolder.networkConnectionState
        .debounce(500)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkState.UnInitialized
        )

    val runnerCount = getRunnerCountUseCase()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onStartButtonClicked() {
        viewModelScope.launch {
            emitEvent(Event.RunningStartButtonClick)
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun startObservingNetworkState() {
        networkConnectionStateHolder.startObservingState()
    }

    fun finishObservingNetworkState() {
        networkConnectionStateHolder.finishObservingState()
    }
}
