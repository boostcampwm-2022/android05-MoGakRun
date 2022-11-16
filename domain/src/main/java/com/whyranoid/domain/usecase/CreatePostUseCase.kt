package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        postContent: String,
        runningHistory: RunningHistory,
        updatedAt: Long
    ): Boolean {
        accountRepository.getUser().onSuccess { user ->
            return postRepository.createPost(user, postContent, runningHistory, updatedAt)
        }.onFailure {
            println("User 정보 없음")
        }
        return false
    }
}
