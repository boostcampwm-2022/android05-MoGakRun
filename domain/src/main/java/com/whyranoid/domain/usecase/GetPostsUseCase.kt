package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.Post
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO : 페이징 처리가 잘 끝나면 삭제
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    operator fun invoke(): Flow<List<Post>> {
        return postRepository.getAllPostFlow()
    }
}
