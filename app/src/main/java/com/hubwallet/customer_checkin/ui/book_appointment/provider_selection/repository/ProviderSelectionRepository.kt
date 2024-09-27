package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.repository

import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.network.ProviderSelectionApi
import javax.inject.Inject

class ProviderSelectionRepository @Inject constructor(private val providerSelectionApi: ProviderSelectionApi) {


    suspend fun getProviderList(
        vendor_id: String,
        type: String,
        service: String,
        start_date: String,
        start_time: String,
        end_time: String
    ) = providerSelectionApi.getStylistList(
        vendor_id,
        type,
        service,
        start_date,
        start_time,
        end_time
    )
}