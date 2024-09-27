package com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model

import com.google.gson.annotations.SerializedName

data class CardResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<Result>?,
    @SerializedName("status")
    val status: Int?
)