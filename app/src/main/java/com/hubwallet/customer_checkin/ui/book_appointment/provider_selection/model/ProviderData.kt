package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model

data class ProviderData(
    var providerName: String,
    var providerId: String = "any",
    var isSelected: Boolean = false,
    val price: String?,
    val image: String = "",
    var note: String
)