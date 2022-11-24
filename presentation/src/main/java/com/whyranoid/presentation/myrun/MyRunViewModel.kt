package com.whyranoid.presentation.myrun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetNicknameUseCase
import com.whyranoid.domain.usecase.GetProfileUriUseCase
import com.whyranoid.domain.usecase.UpdateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRunViewModel @Inject constructor(
    private val getNicknameUseCase: GetNicknameUseCase,
    private val getProfileUriUseCase: GetProfileUriUseCase,
    private val updateNickNameUseCase: UpdateNicknameUseCase
) : ViewModel() {

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String>
        get() = _nickName

    private val _profileImgUri = MutableLiveData<String>()
    val profileImgUri: LiveData<String>
        get() = _profileImgUri

    fun getNickName() {
        viewModelScope.launch {
            getNicknameUseCase().collect {
                _nickName.value = it
            }
        }
    }

    fun getProfileImgUri() {
        viewModelScope.launch {
            getProfileUriUseCase().collect {
                _profileImgUri.value = it
            }
        }
    }

    fun updateNickName(newNickName: String) {
        viewModelScope.launch {
            updateNickNameUseCase(newNickName).onSuccess {
                _nickName.value = it
            }.onFailure {
                // TODO 닉네임 변경 실패시
            }
        }
    }
}
