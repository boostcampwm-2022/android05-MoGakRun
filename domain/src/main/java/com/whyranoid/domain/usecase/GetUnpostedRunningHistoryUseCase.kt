package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.RunningHistoryRepository
import javax.inject.Inject

class GetUnpostedRunningHistoryUseCase @Inject constructor(private val runningHistoryRepository: RunningHistoryRepository) {
    suspend operator fun invoke(): List<RunningHistory> {
        return runningHistoryRepository.getUnpostedRunningHistory()
    }
}
