package com.whyranoid.domain.useCase

import com.whyranoid.domain.repository.fakeGroupRepository
import com.whyranoid.domain.usecase.GetGroupInfoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetGroupInfoUseCaseTest {

    // uid는 상관없이 groupId가 "fake" 일 때 Leader의 name이 hyunsoo인 그룹을 flow로 반환
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGroupInfoUseCase_groupIdIsFake_returnGroupInfo() = runTest {
        // given
        val getGroupInfoUseCase =
            GetGroupInfoUseCase(fakeGroupRepository).invoke(uid = "fake", groupId = "fake")

        // when
        val groupInfo = getGroupInfoUseCase.firstOrNull()

        // then
        assertEquals("hyunsoo", (requireNotNull(groupInfo).leader.name))
    }

    // uid는 상관없이 groupId가 "fake" 가 아닐 때 그룹을 반환하지 않음
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGroupInfoUseCase_groupIdIsNotFake_returnNothing() = runTest {
        // given
        val getGroupInfoUseCase =
            GetGroupInfoUseCase(fakeGroupRepository).invoke(uid = "fake", groupId = "noFake")

        // when
        val groupInfo = getGroupInfoUseCase.firstOrNull()

        // then
        assertNull(groupInfo)
    }
}
