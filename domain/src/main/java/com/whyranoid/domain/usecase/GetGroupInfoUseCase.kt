package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupInfoUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(uid: String, groupId: String): Flow<GroupInfo> {
        return groupRepository.getGroupInfoFlow(uid, groupId)
    }
}
