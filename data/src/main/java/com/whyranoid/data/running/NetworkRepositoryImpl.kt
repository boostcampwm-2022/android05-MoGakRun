package com.whyranoid.data.running

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.whyranoid.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkRepositoryImpl(private val context: Context) :
    NetworkRepository {

    private val _networkConnectionState = MutableStateFlow(getCurrentNetwork())
    override fun getNetworkConnectionState() = _networkConnectionState.asStateFlow()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            emitNetworkConnectionState(true)
        }

        override fun onLost(network: Network) {
            emitNetworkConnectionState(false)
        }
    }
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null && context.isConnected) {
                emitNetworkConnectionState(true)
            } else {
                emitNetworkConnectionState(false)
            }
        }
    }

    override fun addNetworkConnectionCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
        } else {
            context.registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    override fun removeNetworkConnectionCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        } else {
            context.unregisterReceiver(connectivityReceiver)
        }
    }

    private fun getCurrentNetwork(): Boolean {
        return try {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (e: Exception) {
            false
        }
    }

    private fun emitNetworkConnectionState(currentState: Boolean) {
        _networkConnectionState.value = currentState
    }
}

val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true
