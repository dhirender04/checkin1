package com.hubwallet.customer_checkin.ui.cancelation.repository

import com.hubwallet.customer_checkin.ui.cancelation.network.CancellationApi
import javax.inject.Inject

class CancellationRepository @Inject constructor(val api: CancellationApi) {
    suspend fun cancellationApi(vendor_id: String) =
        api.cancellationApi(vendor_id)
}