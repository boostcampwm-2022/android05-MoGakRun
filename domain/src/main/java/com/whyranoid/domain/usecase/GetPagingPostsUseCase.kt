package com.whyranoid.domain.usecase

import androidx.paging.PagingData
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagingPostsUseCase @Inject constructor(private val postRepository: PostRepository) {
    operator fun invoke(coroutineScope: CoroutineScope): Flow<PagingData<Post>> {
        return postRepository.getPagingPosts(coroutineScope)
    }
}
