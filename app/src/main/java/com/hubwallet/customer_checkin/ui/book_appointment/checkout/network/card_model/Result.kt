package com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("card_id")
    val cardId: String?,
    @SerializedName("card_number")
    val cardNumber: String?,
    @SerializedName("card_type")
    val cardType: String?,
    @SerializedName("cardholder_name")
    val cardholderName: String?,
    @SerializedName("customer_id")
    val customerId: String?,
    @SerializedName("cvv")
    val cvv: String?,
    @SerializedName("date_created")
    val dateCreated: String?,
    @SerializedName("expiry_month")
    val expiryMonth: String?,
    @SerializedName("expiry_year")
    val expiryYear: String?,
    @SerializedName("is_default")
    val isDefault: String?, @SerializedName("zipcode")
    val zipcode: String?
)