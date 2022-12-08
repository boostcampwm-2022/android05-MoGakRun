package com.whyranoid

import javax.inject.Inject

class RestoreRunningHistoryDataUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(uid: String): Result<Boolean> {
        return signInRepository.restoreRunningHistoryData(uid)
    }
}
