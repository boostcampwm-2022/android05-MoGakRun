package com.whyranoid.presentation.model

sealed class UiState<out T> {

    object UnInitialized : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T>(val value: T) : UiState<T>()

    data class Failure(val throwable: Throwable) : UiState<Nothing>()
}
