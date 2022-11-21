package com.whyranoid.presentation.runningstart

import androidx.lifecycle.ViewModel
import com.whyranoid.domain.usecase.GetRunnerCountUseCase
import com.whyranoid.presentation.util.networkconnection.NetworkConnectionStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunningStartViewModel @Inject constructor(
    getRunnerCountUseCase: GetRunnerCountUseCase,
    networkConnectionStateHolder: NetworkConnectionStateHolder
) : ViewModel() {

    val networkState = networkConnectionStateHolder.networkState

    val runnerCount = getRunnerCountUseCase()
}
