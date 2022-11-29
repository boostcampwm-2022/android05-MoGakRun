package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.RunningHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRunningHistoryUseCase @Inject constructor(private val runningHistoryRepository: RunningHistoryRepository) {
    operator fun invoke(): Flow<Result<List<RunningHistory>>> {
        return runningHistoryRepository.getRunningHistory()
    }
}
