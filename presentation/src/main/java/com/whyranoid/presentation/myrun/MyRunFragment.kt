package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentMyRunBinding
import com.whyranoid.presentation.databinding.ItemCalendarDayBinding
import com.whyranoid.presentation.util.loadImage
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

@AndroidEntryPoint
internal class MyRunFragment : BaseFragment<FragmentMyRunBinding>(R.layout.fragment_my_run) {

    private val viewModel by viewModels<MyRunViewModel>()

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(requireContext())
    }

    private val runningHistoryAdapter = MyRunningHistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeInfo()
    }

    private fun initViews() = with(binding) {
        ivEditNickName.setOnClickListener {
            popUpEditNickNameDialog()
        }

        rvMyRunningHistory.adapter = runningHistoryAdapter

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_run_setting -> {
                    val direction = MyRunFragmentDirections.actionMyRunFragmentToSettingFragment()
                    findNavController().navigate(direction)
                }
            }
            true
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.tvCalendarDay

                textView.text = day.date.dayOfMonth.toString()
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(240)
        val lastMonth = currentMonth.plusMonths(240)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.apply {
            setup(firstMonth, lastMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
    }

    private fun observeInfo() {
        repeatWhenUiStarted {
            viewModel.nickName.collect { nickName ->
                binding.tvNickName.text = nickName
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImgUri.collect { profileImgUri ->
                binding.ivProfileImage.loadImage(profileImgUri)
            }
        }

        repeatWhenUiStarted {
            viewModel.runningHistoryList.collect { runningHistoryList ->
                runningHistoryAdapter.submitList(runningHistoryList)
            }
        }
    }

    private fun popUpEditNickNameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_nick_name, null)

        if (dialogView.parent != null) {
            builder.show()
        } else {
            builder.setView(dialogView)
                .setPositiveButton(
                    getString(R.string.my_run_edit_nick_name_dialog_positive)
                ) { dialog, _ ->
                    viewModel.updateNickName(
                        viewModel.uid.value,
                        dialogView.findViewById<EditText>(R.id.et_change_nick_name).text.toString()
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(
                    getString(R.string.my_run_edit_nick_name_dialog_negative)
                ) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay
        val binding = ItemCalendarDayBinding.bind(view)
    }
}
