package com.whyranoid.presentation.util.networkconnection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkConnectionStateHolder(context: Context) {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.UnInitialized)
    val networkState: StateFlow<NetworkState> get() = _networkState.asStateFlow()

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _networkState.value =
                if (context.isConnected) NetworkState.Connection else NetworkState.DisConnection
        }
    }

    init {
        context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }
}

val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true
