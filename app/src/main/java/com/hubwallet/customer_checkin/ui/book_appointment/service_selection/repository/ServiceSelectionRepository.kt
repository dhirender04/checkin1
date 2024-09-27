package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.repository

import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.ServiceSelectionApi
import javax.inject.Inject

class ServiceSelectionRepository @Inject constructor(val serviceSelectionApi: ServiceSelectionApi) {
    suspend fun serviceCategory (vendorId: String) = serviceSelectionApi.serviceCategory(vendorId)
    suspend fun getServiceByCategoryId (vendorId: String,categoryId:String) = serviceSelectionApi.getServiceByCategoryId(vendorId,categoryId)
}