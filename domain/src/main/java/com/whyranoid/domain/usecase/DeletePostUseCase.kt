package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(postId: String): Boolean {
        return postRepository.deletePost(postId)
    }
}
