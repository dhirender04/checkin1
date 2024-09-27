package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist

import com.google.gson.annotations.SerializedName

data class ServiceListModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val serviceListResult: List<ServiceListResult>,
    @SerializedName("status")
    val status: Int
)