package com.whyranoid.presentation.util.networkconnection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkConnectionStateHolder(private val context: Context) {

    private val _networkConnectionState = MutableStateFlow<NetworkState>(NetworkState.UnInitialized)
    val networkConnectionState: StateFlow<NetworkState> = _networkConnectionState

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val connectivityCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            emitNetworkConnectionState(NetworkState.Connection)
        }

        override fun onLost(network: Network) {
            emitNetworkConnectionState(NetworkState.DisConnection)
        }
    }
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null && context.isConnected) {
                emitNetworkConnectionState(NetworkState.Connection)
            } else {
                emitNetworkConnectionState(NetworkState.DisConnection)
            }
        }
    }

    fun startObservingState() {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            emitNetworkConnectionState(NetworkState.Connection)
        } else {
            emitNetworkConnectionState(NetworkState.DisConnection)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
        } else {
            context.registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun finishObservingState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        } else {
            context.unregisterReceiver(connectivityReceiver)
        }
    }

    private fun emitNetworkConnectionState(currentState: NetworkState) {
        _networkConnectionState.value = currentState
    }
}

val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true
