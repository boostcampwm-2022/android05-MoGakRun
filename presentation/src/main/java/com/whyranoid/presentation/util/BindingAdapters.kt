package com.whyranoid.presentation.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.whyranoid.presentation.R
import com.whyranoid.presentation.util.networkconnection.NetworkState
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("startLongToDate")
fun TextView.startLongToDate(long: Long) {
    val formatter = SimpleDateFormat("yyyy.MM.dd / HH 시 mm 분 운동 시작", Locale.KOREA)
    text = formatter.format(Date(long))
}

@BindingAdapter("finishLongToDate")
fun TextView.finishLongToDate(long: Long) {
    val formatter = SimpleDateFormat("yyyy.MM.dd / HH 시 mm 분 운동 종료", Locale.KOREA)
    text = formatter.format(Date(long))
}
