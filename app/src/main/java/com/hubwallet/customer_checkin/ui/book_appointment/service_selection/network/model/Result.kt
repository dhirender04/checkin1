package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model

data class Result(
    val category_id: String,
    val category_name: String,
    var isSelected:Boolean = false
)