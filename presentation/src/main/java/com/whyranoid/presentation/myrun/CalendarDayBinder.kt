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

class CalendarDayBinder(
    private val calendarView: CalendarView,
    private val runningDays: List<List<String>>
) : DayBinder<CalendarDayBinder.DayContainer> {

    class DayContainer(
        val binding: ItemCalendarDayBinding
    ) : ViewContainer(binding.root)

    override fun create(view: View): DayContainer =
        DayContainer(ItemCalendarDayBinding.bind(view))

    override fun bind(container: DayContainer, day: CalendarDay) {
        container.binding.tvCalendarDay.text = day.date.dayOfMonth.toString()

        if (day.owner != DayOwner.THIS_MONTH) {
            container.binding.tvCalendarDay.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.gray
                )
            )
            container.binding.root.background = null
        } else {
            container.binding.tvCalendarDay.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.mogakrun_on_secondary
                )
            )
            container.binding.root.background = null
        }

        runningDays.forEach { runningDay ->
            val (runningDayYear, runningDayMonth, runningDayDay) = runningDay.map { it.toInt() }

            if (runningDayYear == day.date.year && runningDayMonth == day.date.monthValue && runningDayDay == day.date.dayOfMonth) {
                container.binding.root.background =
                    ContextCompat.getDrawable(
                        calendarView.context,
                        R.drawable.kong
                    )
            }
        }
    }
}
