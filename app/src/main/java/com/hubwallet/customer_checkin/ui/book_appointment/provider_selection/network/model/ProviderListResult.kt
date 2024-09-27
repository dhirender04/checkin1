package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.network.model

data class ProviderListResult(
    var providerName: String,
    var providerId: String = "any",
    val price: String?,
    val image: String = "",
    var note: String,
    var isSelected: Boolean = false,
)
