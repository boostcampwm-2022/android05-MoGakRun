package com.whyranoid.presentation.myrun

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentMyRunBinding
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
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

    private val currentMonth = YearMonth.now()
    private val firstMonth = currentMonth.minusMonths(MONTH_OFFSET)
    private val lastMonth = currentMonth.plusMonths(MONTH_OFFSET)
    private val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
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
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.nickName.collect { nickName ->
                binding.tvNickName.text = nickName
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.profileImgUri.collect { profileImgUri ->
                binding.ivProfileImage.loadImage(profileImgUri)
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.runningHistoryListState.collect { runningHistoryListState ->
                when (runningHistoryListState) {
                    is UiState.UnInitialized -> {
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success<List<RunningHistoryUiModel>> -> handleRunningHistorySuccessState(
                        runningHistoryListState.value
                    )

                    is UiState.Failure -> {
                    }
                }
            }
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.runningDays.collect { runningDays ->
                binding.calendarView.apply {
                    itemAnimator = null
                    dayBinder = CalendarDayBinder(this, runningDays)
                    monthScrollListener = { calendarMonth ->
                        onMonthScrolled(calendarMonth.yearMonth)
                    }
                    // 모든 달력 범위 설정
                    setup(firstMonth, lastMonth, firstDayOfWeek)
                    // 첫 화면에서 보일 달 설정
                    scrollToMonth(currentMonth)
                }
            }
        }
    }

    private fun handleRunningHistorySuccessState(runningHistoryList: List<RunningHistoryUiModel>) {
        runningHistoryAdapter.submitList(runningHistoryList)
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

    private fun onMonthScrolled(currentMonth: YearMonth) {
        val visibleMonth = binding.calendarView.findFirstVisibleMonth() ?: return
        binding.tvMonthIndicator.text = getString(R.string.my_run_year_month_indicator, visibleMonth.yearMonth.month.toString(), visibleMonth.year.toString())
        if (currentMonth != visibleMonth.yearMonth) {
            binding.calendarView.smoothScrollToMonth(currentMonth)
        }
    }

    companion object {
        private const val MONTH_OFFSET = 120L
    }
}
