package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist

data class ServiceListResult(
    val category_id: String,
    val note: String?,
    val service_duration: String?,
    val service_id: String,
    val service_name: String,
    val service_price: String,
    var isSelected:Boolean = false
)