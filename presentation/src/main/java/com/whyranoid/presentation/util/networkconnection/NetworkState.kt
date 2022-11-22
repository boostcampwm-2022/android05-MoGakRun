package com.whyranoid.presentation.util.networkconnection

sealed class NetworkState {
    object UnInitialized : NetworkState()

    object Connection : NetworkState()

    object DisConnection : NetworkState()
}
