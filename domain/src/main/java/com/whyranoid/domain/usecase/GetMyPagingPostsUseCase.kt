package com.whyranoid.domain.usecase

import androidx.paging.PagingData
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyPagingPostsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(): Flow<PagingData<Post>> {
        return postRepository.getMyPagingPosts(accountRepository.getUid())
    }
}
