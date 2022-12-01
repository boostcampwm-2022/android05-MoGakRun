package com.whyranoid.presentation.community.runningpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetRunningHistoryUseCase
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.model.toRunningHistoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectRunningHistoryViewModel @Inject constructor(private val getRunningHistoryUseCase: GetRunningHistoryUseCase) :
    ViewModel() {

    init {
        getRunningHistoryList()
    }

    private val _runningHistoryListState =
        MutableStateFlow<UiState<List<RunningHistoryUiModel>>>(UiState.UnInitialized)
    val runningHistoryListState: StateFlow<UiState<List<RunningHistoryUiModel>>>
        get() = _runningHistoryListState.asStateFlow()

    // TODO 인증글 작성 넘어가도록 하는 더미데이터 -> 인증글 작성 로직이 완료되면 삭제하겠습니다
    private val _selectedRunningHistory = MutableStateFlow<RunningHistoryUiModel?>(RunningHistoryUiModel("seungmin_history_id", "8995875", "2452", "24524", "134", "124", "23"))
    val selectedRunningHistory: StateFlow<RunningHistoryUiModel?>
        get() = _selectedRunningHistory.asStateFlow()

    private fun getRunningHistoryList() {
        viewModelScope.launch {
            _runningHistoryListState.value = UiState.Loading

            getRunningHistoryUseCase().collect { runningHistoryListResult ->
                runningHistoryListResult.onSuccess { runningHistoryList ->
                    _runningHistoryListState.value =
                        UiState.Success(runningHistoryList.map { runningHistory -> runningHistory.toRunningHistoryUiModel() })
                }.onFailure { throwable ->
                    _runningHistoryListState.value = UiState.Failure(throwable)
                }
            }
        }
    }

    fun getSelectedRunningHistory(): RunningHistoryUiModel? {
        return _selectedRunningHistory.value
    }

    fun setSelectedRunningHistory(runningHistoryUiModel: RunningHistoryUiModel?) {
        _selectedRunningHistory.value = runningHistoryUiModel
    }
}
