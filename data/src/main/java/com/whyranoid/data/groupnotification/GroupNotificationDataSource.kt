package com.whyranoid.data.groupnotification

import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow

interface GroupNotificationDataSource {

    fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>>

    suspend fun notifyRunningStart(uid: String, groupIdList: List<String>)

    suspend fun notifyRunningFinish(
        uid: String,
        runningHistory: RunningHistory,
        groupIdList: List<String>
    )
}
