package com.whyranoid

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val saveLogInUserInfoUseCase: SaveLogInUserInfoUseCase
) : ViewModel() {
    suspend fun saveUserInfo(userInfo: SignInUserInfo) {
        saveLogInUserInfoUseCase(userInfo)
    }
}
