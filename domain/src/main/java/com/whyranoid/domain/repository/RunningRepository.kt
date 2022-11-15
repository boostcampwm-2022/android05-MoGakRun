package com.whyranoid.domain.repository

import kotlinx.coroutines.flow.Flow

interface RunningRepository {

    // 현재 달리고 있는 사람 카운트 - 리모트
    fun getCurrentRunnerCount(): Flow<Int>

    // 내가 달리기를 시작할 때 카운트 올려주고 가입한 그룹에 시작 알림 전달 - 리모트
    // 로컬에서는 내가 가입한 그룹 정보만, 서버에서는 그룹에 누가 속했는지
    suspend fun startRunning(uid: String): Boolean

    // 내가 달리기를 종료할 때 카운트 내려주기 - 리모트
    suspend fun finishRunning(uid: String): Boolean
}
