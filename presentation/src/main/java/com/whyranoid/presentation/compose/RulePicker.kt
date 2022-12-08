package com.whyranoid.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whyranoid.presentation.R
import com.whyranoid.presentation.community.group.create.CreateGroupViewModel

@Composable
fun RulePicker(
    declarationDialogState: Boolean,
    viewModel: CreateGroupViewModel
) {
    val emptyText = stringResource(id = R.string.text_empty)
    val confirmText = stringResource(id = R.string.text_confirm)
    val cancelText = stringResource(id = R.string.text_cancel)
    val dialogTitleText = stringResource(id = R.string.text_rule_picker_title)

    var selectedDate by remember { mutableStateOf(emptyText) }
    var selectedHour by remember { mutableStateOf(emptyText) }
    var selectedMinute by remember { mutableStateOf(emptyText) }

    if (declarationDialogState) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onDialogDismiss()
            },
            title = {
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dialogTitleText,
                        color = colorResource(id = R.color.mogakrun_onBackground),
                        fontWeight = Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        DateDropDownMenu(selectedDate) { date ->
                            selectedDate = date
                        }
                        HourDropDownMenu(selectedHour) { hour ->
                            selectedHour = hour
                        }
                        MinuteDropDownMenu(selectedMinute) { minute ->
                            selectedMinute = minute
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDialogConfirm(selectedDate, selectedHour, selectedMinute)
                        selectedDate = emptyText
                        selectedHour = emptyText
                        selectedMinute = emptyText
                    }
                ) {
                    Text(
                        text = confirmText,
                        color = colorResource(id = R.color.mogakrun_on_primary)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onDialogDismiss()
                        selectedDate = emptyText
                        selectedHour = emptyText
                        selectedMinute = emptyText
                    }

                ) {
                    Text(
                        text = cancelText,
                        color = colorResource(id = R.color.mogakrun_on_primary)
                    )
                }
            },
            backgroundColor = colorResource(id = R.color.mogakrun_background),
            contentColor = colorResource(id = R.color.mogakrun_on_primary),
            shape = RoundedCornerShape(10)
        )
    }
}
