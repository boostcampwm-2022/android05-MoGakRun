package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import javax.inject.Inject

class CreateRecruitPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        groupUid: String
    ): Boolean {
        val uid = accountRepository.getUid()
        return postRepository.createRecruitPost(uid, groupUid)
    }
}
