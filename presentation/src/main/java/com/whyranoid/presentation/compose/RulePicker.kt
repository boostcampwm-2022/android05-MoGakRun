package com.whyranoid.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.community.group.create.CreateGroupViewModel

@Composable
fun RulePicker(
    declarationDialogState: Boolean,
    viewModel: CreateGroupViewModel
) {
    var selectedDate by remember { mutableStateOf("요일") }
    var selectedHour by remember { mutableStateOf("시간") }
    var selectedMinute by remember { mutableStateOf("분") }
    if (declarationDialogState) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onDialogDismiss()
            },
            title = {
                Text(text = "요일 및 시간을 골라주세요!!")
            },
            text = {
                Column {
                    DateDropDownMenu(selectedDate) { date ->
                        selectedDate = date
                    }
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    HourDropDownMenu(selectedHour) { hour ->
                        selectedHour = hour
                    }
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    MinuteDropDownMenu(selectedMinute) { minute ->
                        selectedMinute = minute
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDialogConfirm(selectedDate, selectedHour, selectedMinute)
                        selectedDate = "요일"
                        selectedHour = "시간"
                        selectedMinute = "분"
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onDialogDismiss()
                        selectedDate = "요일"
                        selectedHour = "시간"
                        selectedMinute = "분"
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }
}
