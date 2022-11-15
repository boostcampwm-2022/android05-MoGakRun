package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.User
import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(
        user: User,
        postContent: String,
        runningHistory: RunningHistory,
        updatedAt: Long
    ): Boolean {
        return postRepository.createPost(user, postContent, runningHistory, updatedAt)
    }
}
