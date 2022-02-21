package com.example.visioglobe.util.impl

import androidx.annotation.VisibleForTesting
import com.example.visioglobe.util.DateFormatter
import java.text.SimpleDateFormat
import java.util.*


class DateFormatterImpl : DateFormatter {

    override fun mapToDateString(date: Long): String {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return dateFormat.format(date)
    }

    companion object {
        private const val DATE_PATTERN = "dd/MM/yy hh:mm a"
    }

}