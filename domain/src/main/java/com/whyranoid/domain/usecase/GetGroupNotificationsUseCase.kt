package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupNotificationsUseCase @Inject constructor(private val groupRepository: GroupRepository) {

    operator fun invoke(groupId: String): Flow<List<GroupNotification>> {
        return groupRepository.getGroupNotifications(groupId)
    }
}
