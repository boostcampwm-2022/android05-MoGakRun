package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.RunningRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentRunnerCountUseCase @Inject constructor(private val runningRepository: RunningRepository) {
    operator fun invoke(): Flow<Int> {
        return runningRepository.getCurrentRunnerCount()
    }
}
