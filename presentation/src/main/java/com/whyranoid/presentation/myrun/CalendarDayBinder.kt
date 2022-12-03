package com.whyranoid.presentation.myrun

import android.view.View
import androidx.core.content.ContextCompat
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.whyranoid.presentation.R
import com.whyranoid.presentation.databinding.ItemCalendarDayBinding
import java.time.LocalDate

class CalendarDayBinder(
    private val calendarView: CalendarView,
    private val runningDays: List<List<String>>
) : DayBinder<CalendarDayBinder.DayContainer> {
    private var calendar: CalendarRange = CalendarRange(null, null)

    class DayContainer(
        val binding: ItemCalendarDayBinding
    ) : ViewContainer(binding.root)

    override fun create(view: View): DayContainer =
        DayContainer(ItemCalendarDayBinding.bind(view))

    override fun bind(container: DayContainer, day: CalendarDay) {
        val (startDate, endDate) = this.calendar

        container.binding.tvCalendarDay.text = day.date.dayOfMonth.toString()

        if (day.owner != DayOwner.THIS_MONTH) {
            container.binding.tvCalendarDay.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.gray
                )
            )
            // day.day와 day.date.monthValue를 지정해서 특정 월, 일에 달렸다는 콩 표시 가능
        } else {
            container.binding.tvCalendarDay.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.black
                )
            )
            container.binding.root.background = null
        }

        if (isInRange(day.date)) {
            container.binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.gray
                )
            )
        }

        if (startDate == day.date) {
            container.binding.root.background =
                ContextCompat.getDrawable(
                    calendarView.context,
                    R.drawable.thumbnail_src_small
                )
        } else if (endDate == day.date) {
            container.binding.root.background =
                ContextCompat.getDrawable(
                    calendarView.context,
                    R.drawable.thumbnail_src_small
                )
        }

        runningDays.forEach { runningDay ->
            val (runningDayYear, runningDayMonth, runningDayDay) = runningDay.map { it.toInt() }

            if (runningDayYear == day.date.year && runningDayMonth == day.date.monthValue && runningDayDay == day.day) {
                container.binding.root.background =
                    ContextCompat.getDrawable(
                        calendarView.context,
                        R.drawable.calendar_kong
                    )
            }
        }
    }

    private fun isInRange(date: LocalDate): Boolean {
        val (startDate, endDate) = this.calendar
        return startDate == date || endDate == date || (startDate != null && endDate != null && startDate < date && date < endDate)
    }
}

data class CalendarRange(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)
