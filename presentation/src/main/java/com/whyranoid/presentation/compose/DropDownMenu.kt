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

@Composable
fun DateDropDownMenu(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val dateList = "월 화 수 목 금 토 일".split(" ").toList()
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
    val hourList =
        "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23".split(" ").toList()
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
    val minuteList =
        "0 5 10 15 20 25 30 35 40 45 50 55 60".split(" ").toList()
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
