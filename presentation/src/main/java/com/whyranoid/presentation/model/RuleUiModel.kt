package com.whyranoid.presentation.model

import android.os.Parcelable
import com.whyranoid.domain.model.DayOfWeek
import com.whyranoid.domain.model.Rule
import kotlinx.parcelize.Parcelize

@Parcelize
data class RuleUiModel(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int
) : Parcelable {
    override fun toString(): String {
        return "${dayOfWeek.dayResId}-$hour-$minute"
    }
}

fun Rule.toRuleUiModel() =
    RuleUiModel(
        dayOfWeek = this.dayOfWeek,
        hour = this.hour,
        minute = this.minute
    )
