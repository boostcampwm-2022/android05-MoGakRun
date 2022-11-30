package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class CreateRecruitPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository
) {
    // TODO : accountRepository에서 User를 가져오도록 수정
    suspend operator fun invoke(
        authorUid: String,
        groupUid: String
    ): Boolean {
        return postRepository.createRecruitPost(authorUid, groupUid)
    }
}
