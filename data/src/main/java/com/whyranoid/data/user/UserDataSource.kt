package com.whyranoid.data.user

import com.whyranoid.domain.model.GroupInfo
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>>

    fun getMyGroupListFlow(uid: String): Flow<List<GroupInfo>>
}
