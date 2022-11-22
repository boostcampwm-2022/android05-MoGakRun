package com.whyranoid.presentation.runningstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetRunnerCountUseCase
import com.whyranoid.domain.usecase.StartRunningUseCase
import com.whyranoid.presentation.util.networkconnection.NetworkConnectionStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningStartViewModel @Inject constructor(
    private val startRunningUseCase: StartRunningUseCase,
    getRunnerCountUseCase: GetRunnerCountUseCase,
    networkConnectionStateHolder: NetworkConnectionStateHolder
) : ViewModel() {

    val networkState = networkConnectionStateHolder.networkState

    val runnerCount = getRunnerCountUseCase()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onStartButtonClicked() {
        viewModelScope.launch {
            if (startRunningUseCase()) {
                emitEvent(Event.RunningStartButtonClick)
            } else {
                // TODO: 러닝 시작 실패 피드백 주기
            }
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
