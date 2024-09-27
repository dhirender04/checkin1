package com.hubwallet.customer_checkin.ui.login.network.model

data class BusinessInfo(
    val address: String,
    val business_name: String,
    val city: String,
    val email: String,
    val phone: String,
    val photo: String,
    val state: Any,
    val zipcode: String
)