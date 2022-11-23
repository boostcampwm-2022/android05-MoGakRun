package com.whyranoid.presentation.myrun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetNicknameUseCase
import com.whyranoid.domain.usecase.GetProfileUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyRunViewModel @Inject constructor(
    private val getNicknameUseCase: GetNicknameUseCase,
    private val getProfileUriUseCase: GetProfileUriUseCase
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
                Timber.d("유저 닉네임 불러오기 $it")
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
}
