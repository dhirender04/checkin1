package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model

data class StylistTime(
    val message: String,
    val status: Int,
    val result: Map<String, List<String>>,
    val stylist_appointment: Map<String, Map<String, String>>,
    val customer_appointment: Map<String, Map<String, String>>
)