package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class UpdatePostRepository @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(postId: String, postContent: String, updatedAt: Long): Boolean {
        return postRepository.updatePost(postId, postContent, updatedAt)
    }
}
