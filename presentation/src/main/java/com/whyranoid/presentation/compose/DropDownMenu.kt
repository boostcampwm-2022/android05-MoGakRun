package com.whyranoid.presentation.compose

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.whyranoid.presentation.R

@Composable
fun DateDropDownMenu(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val dateList = stringArrayResource(id = R.array.rule_date_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = selectedDate)
    }

    DropdownMenu(
        modifier = Modifier
            .wrapContentSize(),
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        dateList.forEach { date ->
            DropdownMenuItem(onClick = {
                onDateSelected(date)
                isDropDownMenuExpanded = false
            }) {
                Text(text = date)
            }
        }
    }
}

@Composable
fun HourDropDownMenu(
    selectedHour: String,
    onHourSelected: (String) -> Unit
) {
    val hourList = stringArrayResource(id = R.array.rule_hour_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = selectedHour)
    }

    DropdownMenu(
        modifier = Modifier
            .wrapContentSize(),
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        hourList.forEach { hour ->
            DropdownMenuItem(onClick = {
                onHourSelected(hour)
                isDropDownMenuExpanded = false
            }) {
                Text(text = hour)
            }
        }
    }
}

@Composable
fun MinuteDropDownMenu(
    selectedMinute: String,
    onMinuteSelected: (String) -> Unit
) {
    val minuteList = stringArrayResource(id = R.array.rule_minute_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = selectedMinute)
    }

    DropdownMenu(
        modifier = Modifier
            .wrapContentSize(),
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        minuteList.forEach { minute ->
            DropdownMenuItem(onClick = {
                onMinuteSelected(minute)
                isDropDownMenuExpanded = false
            }) {
                Text(text = minute)
            }
        }
    }
}
