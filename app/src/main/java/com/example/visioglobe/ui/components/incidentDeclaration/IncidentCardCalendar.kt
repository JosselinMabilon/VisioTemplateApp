package com.example.visioglobe.ui.components.incidentDeclaration

import android.os.Build
import android.text.format.DateFormat.is24HourFormat
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentManager
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentDeclarationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncidentDateField(
    supportFragmentManager: FragmentManager,
    viewModel: IncidentDeclarationViewModel
) {

    val datePicker = getDatePicker().build()
    val incidentDate: Calendar? by viewModel.incidentDate.observeAsState()

    datePicker.addOnPositiveButtonClickListener { day ->
        val calendar = Calendar.getInstance()
        val calendarUpdate: Calendar = (incidentDate?.clone() as Calendar)

        calendar.time = Date(day)
        calendarUpdate.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        viewModel.onDateChange(calendarUpdate)
    }
    ReadOnlyIncidentTextField(
        value = (stringResource(
            id = R.string.field_day,
            String.format(
                stringResource(R.string.date_format),
                incidentDate?.get(Calendar.DAY_OF_MONTH)
            ),
            String.format(stringResource(R.string.date_format), incidentDate?.get(Calendar.MONTH)),
            incidentDate?.get(Calendar.YEAR).toString()
        )),
        onClick = {
            datePicker.show(supportFragmentManager, datePicker.toString())
        },
        placeholder = stringResource(id = R.string.field_date),
        modifier = Modifier.fillMaxWidth(0.5f)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncidentTimeField(
    supportFragmentManager: FragmentManager,
    viewModel: IncidentDeclarationViewModel
) {
    val usesSystem24HFormat = is24HourFormat(LocalContext.current)
    val timeFormat = if (usesSystem24HFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
    val timePicker = getTimePicker().setTimeFormat(timeFormat).build()
    val incidentDate: Calendar? by viewModel.incidentDate.observeAsState()

    timePicker.addOnPositiveButtonClickListener {
        val calendarUpdate: Calendar = (incidentDate?.clone() as Calendar)

        calendarUpdate.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendarUpdate.set(Calendar.MINUTE, timePicker.minute)
        viewModel.onDateChange(calendarUpdate)
    }
    ReadOnlyIncidentTextField(
        value = stringResource(
            R.string.field_hour,
            String.format("%02d", incidentDate?.get(Calendar.HOUR_OF_DAY)),
            String.format("%02d", incidentDate?.get(Calendar.MINUTE))
        ),
        onClick = {
            timePicker.show(supportFragmentManager, timePicker.toString())
        },
        placeholder = stringResource(id = R.string.field_time),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ReadOnlyIncidentTextField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String
) {
    TextField(
        shape = RoundedCornerShape(VisioGlobeAppTheme.dims.corner_shape_button),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_small),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            )
        },
        enabled = false,
        value = value,
        onValueChange = { },
        modifier = modifier
            .padding(
                top = VisioGlobeAppTheme.dims.padding_small,
                end = VisioGlobeAppTheme.dims.padding_normal
            )
            .clickable(onClick = onClick),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            disabledTextColor = MaterialTheme.colors.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

fun getTimePicker(): MaterialTimePicker.Builder {
    val currentDateTime = Calendar.getInstance()
    return MaterialTimePicker.Builder()
        .setHour(currentDateTime.get(Calendar.HOUR_OF_DAY))
        .setMinute(currentDateTime.get(Calendar.MINUTE))
}

fun getDatePicker(): MaterialDatePicker.Builder<Long> {
    return MaterialDatePicker.Builder.datePicker()
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
}




