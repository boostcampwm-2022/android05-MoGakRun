package com.whyranoid.presentation.myrun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.usecase.GetNicknameUseCase
import com.whyranoid.domain.usecase.GetProfileUriUseCase
import com.whyranoid.domain.usecase.GetRunningHistoryUseCase
import com.whyranoid.domain.usecase.UpdateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRunViewModel @Inject constructor(
    private val getNicknameUseCase: GetNicknameUseCase,
    private val getProfileUriUseCase: GetProfileUriUseCase,
    private val updateNickNameUseCase: UpdateNicknameUseCase,
    private val getRunningHistoryUseCase: GetRunningHistoryUseCase
) : ViewModel() {

    private val EMPTY_STRING = ""

    private val _nickName = MutableStateFlow(EMPTY_STRING)
    val nickName: StateFlow<String>
        get() = _nickName.asStateFlow()

    private val _profileImgUri = MutableStateFlow(EMPTY_STRING)
    val profileImgUri: StateFlow<String>
        get() = _profileImgUri.asStateFlow()

    private val _runningHistoryList = MutableStateFlow<List<RunningHistory>>(emptyList())
    val runningHistoryList: StateFlow<List<RunningHistory>>
        get() = _runningHistoryList.asStateFlow()

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

    private fun getRunningHistoryList() {
        viewModelScope.launch {
            getRunningHistoryUseCase().collect { runningHistoryList ->
                _runningHistoryList.value = runningHistoryList
            }
        }
    }
}
