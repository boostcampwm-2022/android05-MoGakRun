package com.whyranoid

import javax.inject.Inject

class SaveLogInUserInfoUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke(userInfo: SignInUserInfo): Boolean {
        return signInRepository.saveLogInUserInfo(userInfo)
    }
}
