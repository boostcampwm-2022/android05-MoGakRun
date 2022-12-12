package com.whyranoid.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.R

@Composable
fun DateDropDownMenu(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val dateText = stringResource(id = R.string.text_date)
    val dateList = stringArrayResource(id = R.array.rule_date_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    Column {
        Button(
            onClick = { isDropDownMenuExpanded = true },
            colors = ButtonDefaults
                .outlinedButtonColors(
                    contentColor = colorResource(id = R.color.mogakrun_on_primary)
                ),
            shape = RoundedCornerShape(30),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(text = selectedDate + dateText)
        }

        DropdownMenu(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .heightIn(
                    0.dp,
                    LocalConfiguration.current.screenHeightDp.dp / 2 - ButtonDefaults.MinHeight
                ),
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false }
        ) {
            dateList.forEach { date ->
                DropdownMenuItem(onClick = {
                    onDateSelected(date)
                    isDropDownMenuExpanded = false
                }) {
                    Text(text = date + dateText)
                }
            }
        }
    }
}

@Composable
fun HourDropDownMenu(
    selectedHour: String,
    onHourSelected: (String) -> Unit
) {
    val hourText = stringResource(id = R.string.text_hour)
    val hourList = stringArrayResource(id = R.array.rule_hour_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    Column {
        Button(
            onClick = { isDropDownMenuExpanded = true },
            colors = ButtonDefaults
                .outlinedButtonColors(
                    contentColor = colorResource(id = R.color.mogakrun_on_primary)
                ),
            shape = RoundedCornerShape(30),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(text = selectedHour + hourText)
        }

        DropdownMenu(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .heightIn(
                    0.dp,
                    LocalConfiguration.current.screenHeightDp.dp / 2 - ButtonDefaults.MinHeight
                ),
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false }
        ) {
            hourList.forEach { hour ->
                DropdownMenuItem(onClick = {
                    onHourSelected(hour)
                    isDropDownMenuExpanded = false
                }) {
                    Text(text = hour + hourText)
                }
            }
        }
    }
}

@Composable
fun MinuteDropDownMenu(
    selectedMinute: String,
    onMinuteSelected: (String) -> Unit
) {
    val minuteText = stringResource(id = R.string.text_minute)
    val minuteList = stringArrayResource(id = R.array.rule_minute_array)
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { isDropDownMenuExpanded = true },
            colors = ButtonDefaults
                .outlinedButtonColors(
                    contentColor = colorResource(id = R.color.mogakrun_on_primary)
                ),
            shape = RoundedCornerShape(30),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(text = selectedMinute + minuteText)
        }

        DropdownMenu(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .heightIn(
                    0.dp,
                    LocalConfiguration.current.screenHeightDp.dp / 2 - ButtonDefaults.MinHeight
                ),
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false }
        ) {
            minuteList.forEach { minute ->
                DropdownMenuItem(onClick = {
                    onMinuteSelected(minute)
                    isDropDownMenuExpanded = false
                }) {
                    Text(text = minute + minuteText)
                }
            }
        }
    }
}
