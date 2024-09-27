package com.hubwallet.customer_checkin.common.base

import android.annotation.SuppressLint
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


object Keys {
    const val emailPattern = "[a-zA-Z0-9._\\-*+#$%&!~`]+@[a-z]+\\.+[a-z]+"
     @SuppressLint("StaticFieldLeak")
    var timePickerBuilder: MaterialTimePicker? = null
    var datePickerBuilder: MaterialDatePicker<Long>? = null

    fun dataPicker() {
        val dateValidator: DateValidator = DateValidatorPointForward.now() // disable previous dates
        datePickerBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setValidator(dateValidator).build()
                )
                .build()
    }

    fun timePicker() {
        timePickerBuilder = MaterialTimePicker.Builder()
            .setTitleText("SELECT YOUR TIMING")
            .setHour(12)
            // set the default minute for the
            // dialog when the dialog opens
            .setMinute(10)
            // set the time format
            // according to the region
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()
    }

}