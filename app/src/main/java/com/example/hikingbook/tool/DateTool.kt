package com.example.hikingbook.tool

import java.text.SimpleDateFormat
import java.util.*


fun Long.toStringDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return format.format(date)
}

fun String.toLongDate(): Long {
    val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val date = format.parse(this)
    return date?.time ?: 0L
}