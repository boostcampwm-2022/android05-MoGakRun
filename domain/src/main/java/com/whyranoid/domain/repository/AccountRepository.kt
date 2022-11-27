package com.whyranoid.domain.repository

import com.whyranoid.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    // 로컬에서 유저 정보 가져오기
    suspend fun getUser(): Result<User>

    // 파이어베이스에서 uid, 닉네임, 프로필사진 가져오기
    suspend fun loginUser(): Boolean

    // 데이터스토어에서 uid 가져오기
    fun getUid(): Flow<String>

    // 데이터스토어에서 닉네임 가져오기
    fun getNickname(): Flow<String>

    // 데이터스토어에서 이메일 가져오기
    fun getEmail(): Flow<Result<String>>

    // 닉네임 수정, 서버에 먼저 보내고 성공하면 로컬에 반영
    // 실패하면 실패 사용자에게 알리기
    suspend fun updateNickname(uid: String, newNickName: String): Result<String>

    // 데이터스토어에서 프로필 이미지 가져오기
    fun getProfileUri(): Flow<String>

    // 프로필 사진 서버에 업데이트
    suspend fun updateProfileUrl(newProfileUrl: String): Boolean

    // 로그아웃
    suspend fun signOut(): Result<Boolean>

    // 회원탈퇴
    suspend fun withDrawal(): Result<Boolean>
}
