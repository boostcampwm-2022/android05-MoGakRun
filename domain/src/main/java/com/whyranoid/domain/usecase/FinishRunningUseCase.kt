package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.RunningRepository
import javax.inject.Inject

class FinishRunningUseCase @Inject constructor(private val runningRepository: RunningRepository) {
    suspend operator fun invoke(uid: String): Boolean {
        return runningRepository.finishRunning(uid)
    }
}
