package com.whyranoid.domain.useCase

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.User
import com.whyranoid.domain.repository.GroupRepository
import com.whyranoid.domain.usecase.GetGroupInfoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GetGroupInfoUseCaseTest {

    @Before
    fun setUp() {

        mockedGroupRepository = mock(GroupRepository::class.java)

        `when`(
            mockedGroupRepository.getGroupInfoFlow(
                uid = FAKE,
                groupId = IS_SUCCESS
            )
        ).thenReturn(flow { emit(MOCKED_GROUP_INFO) })

        `when`(
            mockedGroupRepository.getGroupInfoFlow(
                uid = FAKE,
                groupId = IS_FAILURE
            )
        ).thenReturn(flow { })
    }

    // uid는 상관없이 groupId가 "fake" 일 때 Leader의 name이 hyunsoo인 그룹을 flow로 반환
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGroupInfoUseCase_groupIdIsFake_returnGroupInfo() = runTest {
        // given
        val getGroupInfoUseCase =
            GetGroupInfoUseCase(mockedGroupRepository).invoke(uid = FAKE, groupId = IS_SUCCESS)

        // when
        val groupInfo = getGroupInfoUseCase.firstOrNull()

        // then
        assertEquals(IS_SUCCESS, (requireNotNull(groupInfo).leader.name))
    }

    // uid는 상관없이 groupId가 "fake" 가 아닐 때 그룹을 반환하지 않음
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGroupInfoUseCase_groupIdIsNotFake_returnNothing() = runTest {
        // given
        val getGroupInfoUseCase =
            GetGroupInfoUseCase(mockedGroupRepository).invoke(uid = FAKE, groupId = IS_FAILURE)

        // when
        val groupInfo = getGroupInfoUseCase.firstOrNull()

        // then
        assertNull(groupInfo)
    }

    companion object {

        private lateinit var mockedGroupRepository: GroupRepository
        private const val FAKE = "fake"
        private const val IS_SUCCESS = "isSuccess"
        private const val IS_FAILURE = "isFailure"

        val MOCKED_GROUP_INFO = GroupInfo(
            name = FAKE,
            groupId = FAKE,
            introduce = FAKE,
            rules = emptyList(),
            headCount = 0,
            leader = User(
                uid = FAKE,
                name = IS_SUCCESS,
                profileUrl = FAKE
            )
        )
    }
}
