package com.hubwallet.customer_checkin.ui.cancelation.network.model

data class CancellationModel(
    val message: String,
    val result: List<Result>,
    val status: Int
)