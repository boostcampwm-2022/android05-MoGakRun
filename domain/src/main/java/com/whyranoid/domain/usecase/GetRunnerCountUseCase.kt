package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.RunnerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRunnerCountUseCase @Inject constructor(private val runnerRepository: RunnerRepository) {
    operator fun invoke(): Flow<Result<Int>> {
        return runnerRepository.getCurrentRunnerCount()
    }
}
