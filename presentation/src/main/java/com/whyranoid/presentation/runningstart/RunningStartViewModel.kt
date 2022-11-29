package com.whyranoid.presentation.runningstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetRunnerCountUseCase
import com.whyranoid.presentation.util.networkconnection.NetworkConnectionStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningStartViewModel @Inject constructor(
    getRunnerCountUseCase: GetRunnerCountUseCase,
    networkConnectionStateHolder: NetworkConnectionStateHolder
) : ViewModel() {

    val networkState = networkConnectionStateHolder.networkState

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
}
