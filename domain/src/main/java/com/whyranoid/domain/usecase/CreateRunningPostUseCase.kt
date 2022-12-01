package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class CreateRunningPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        postContent: String,
        runningHistory: RunningHistory
    ): Result<Boolean> {
        return postRepository.createRunningPost(accountRepository.getUid(), runningHistory, postContent)
    }
}
