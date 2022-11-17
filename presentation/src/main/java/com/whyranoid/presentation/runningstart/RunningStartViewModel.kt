package com.whyranoid.presentation.runningstart

import androidx.lifecycle.ViewModel
import com.whyranoid.domain.usecase.GetRunnerCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunningStartViewModel @Inject constructor(
    getRunnerCountUseCase: GetRunnerCountUseCase
) : ViewModel() {

    val runnerCount = getRunnerCountUseCase()
}
