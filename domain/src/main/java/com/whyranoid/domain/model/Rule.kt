package com.whyranoid.domain.model

data class Rule(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int
) {
    override fun toString(): String {
        return "${dayOfWeek.dayResId}-$hour-$minute"
    }
}

fun String.toRule(): Rule {
    val ruleString = this.split("-")
    return Rule(
        dayOfWeek = DayOfWeek.values().find { it.dayResId == ruleString[0] } ?: DayOfWeek.SUN,
        hour = ruleString[1].toInt(),
        minute = ruleString[2].toInt()
    )
}

enum class DayOfWeek(val dayResId: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일")
}
