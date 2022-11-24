package com.whyranoid.presentation.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.whyranoid.presentation.R
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

@BindingAdapter("loadImage")
fun ImageView.loadImage(uri: String) {
    Glide.with(this.context)
        .load(uri)
        .error(R.drawable.thumbnail_src_small)
        .circleCrop()
        .into(this)
}
