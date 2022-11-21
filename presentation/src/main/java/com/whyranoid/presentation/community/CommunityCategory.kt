package com.whyranoid.presentation.community

import androidx.annotation.StringRes
import com.whyranoid.presentation.R

enum class CommunityCategory(@StringRes val stringId: Int) {
    BOARD(R.string.text_board),
    MY_GROUP(R.string.text_my_group),
    MY_POST(R.string.text_my_post)
}
