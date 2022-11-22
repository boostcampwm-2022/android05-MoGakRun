package com.whyranoid.presentation.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.whyranoid.presentation.util.networkconnection.NetworkState

@BindingAdapter("networkConnectionVisibility")
fun View.networkConnectionVisibility(networkState: NetworkState) {
    visibility = when (networkState) {
        is NetworkState.UnInitialized -> View.GONE
        is NetworkState.Connection -> View.GONE
        is NetworkState.DisConnection -> View.VISIBLE
    }
}

@BindingAdapter("enableWithNetworkState")
fun View.enableWithNetworkState(networkState: NetworkState) {
    isEnabled = when (networkState) {
        is NetworkState.UnInitialized -> true
        is NetworkState.Connection -> false
        is NetworkState.DisConnection -> true
    }
}
