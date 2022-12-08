package com.whyranoid.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface NetworkRepository {

    fun getNetworkConnectionState(): StateFlow<Boolean>

    fun addNetworkConnectionCallback()

    fun removeNetworkConnectionCallback()
}
