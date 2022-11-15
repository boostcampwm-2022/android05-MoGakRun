package com.whyranoid.domain.model

data class Rule(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int
)

enum class DayOfWeek(val dayResId: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일")
}
