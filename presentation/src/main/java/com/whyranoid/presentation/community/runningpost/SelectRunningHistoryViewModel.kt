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

    private val _selectedRunningHistory = MutableStateFlow<RunningHistoryUiModel?>(null)
    val selectedRunningHistory: StateFlow<RunningHistoryUiModel?>
        get() = _selectedRunningHistory.asStateFlow()

    private val _selectedItemPosition = MutableStateFlow(NOTHING_SELECTED_ITEM_POSITION)
    val selectedItemPosition: StateFlow<Int>
        get() = _selectedItemPosition.asStateFlow()

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

    fun setSelectedItemPosition(itemPosition: Int) {
        _selectedItemPosition.value = itemPosition
    }

    companion object {
        private const val NOTHING_SELECTED_ITEM_POSITION = -1
    }
}
