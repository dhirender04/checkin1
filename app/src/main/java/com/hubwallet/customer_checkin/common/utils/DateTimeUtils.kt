package com.hubwallet.customer_checkin.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    val visibleUiDateFormatter = SimpleDateFormat("MMM dd yyyy", Locale.US)
    val visibleUiDateFormatter2 = SimpleDateFormat("E M/d ", Locale.US)
    val timeFormatter12 = SimpleDateFormat("hh:mm a", Locale.US)
    val timeFormatter12a = SimpleDateFormat("h:mm a", Locale.US)
    val timeFormatter24 = SimpleDateFormat("HH:mm", Locale.US)
    val timeFormatterHM = SimpleDateFormat("HH:mm", Locale.US)
    val normalDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val availableTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    val normalDateFormatter2 = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    val workingHourFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val summaryDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    val homeDateFormat = SimpleDateFormat("M/d", Locale.US)
    val dateFormatter2_ = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()) //03/09/2022
    val dateFormatter_2 = SimpleDateFormat("M-d-yyyy", Locale.getDefault()) //03/09/2022
    val normalDateFormatter_2 = SimpleDateFormat("M/d/yyyy", Locale.getDefault())



}