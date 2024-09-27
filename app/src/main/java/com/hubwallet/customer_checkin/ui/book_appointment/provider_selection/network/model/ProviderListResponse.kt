package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.network.model

import com.google.gson.annotations.SerializedName

data class ProviderListResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("result")
    val result: HashMap<String, List<ProviderListResult>>,
    @SerializedName("message")
    val message: String,

)