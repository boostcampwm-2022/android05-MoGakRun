package com.whyranoid.presentation.myrun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.usecase.GetNicknameUseCase
import com.whyranoid.domain.usecase.GetProfileUriUseCase
import com.whyranoid.domain.usecase.GetRunningHistoryUseCase
import com.whyranoid.domain.usecase.GetUidUseCase
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
    private val getRunningHistoryUseCase: GetRunningHistoryUseCase,
    private val getUidUseCase: GetUidUseCase
) : ViewModel() {

    init {
        getUid()
        getNickName()
        getProfileImgUri()
    }

    private val EMPTY_STRING = ""

    private val _uid = MutableStateFlow(EMPTY_STRING)
    val uid: StateFlow<String>
        get() = _uid.asStateFlow()

    private val _nickName = MutableStateFlow(EMPTY_STRING)
    val nickName: StateFlow<String>
        get() = _nickName.asStateFlow()

    private val _profileImgUri = MutableStateFlow(EMPTY_STRING)
    val profileImgUri: StateFlow<String>
        get() = _profileImgUri.asStateFlow()

    private val _runningHistoryList = MutableStateFlow<List<RunningHistory>>(emptyList())
    val runningHistoryList: StateFlow<List<RunningHistory>>
        get() = _runningHistoryList.asStateFlow()

    private fun getUid() {
        viewModelScope.launch {
            getUidUseCase().collect {
                _uid.value = it
            }
        }
    }

    private fun getNickName() {
        viewModelScope.launch {
            getNicknameUseCase().collect {
                _nickName.value = it
            }
        }
    }

    private fun getProfileImgUri() {
        viewModelScope.launch {
            getProfileUriUseCase().collect {
                _profileImgUri.value = it
            }
        }
    }

    fun updateNickName(uid: String, newNickName: String) {
        viewModelScope.launch {
            updateNickNameUseCase(uid, newNickName).onSuccess {
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
