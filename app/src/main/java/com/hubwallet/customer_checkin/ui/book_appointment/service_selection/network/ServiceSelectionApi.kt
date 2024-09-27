package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network

import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.ServiceCategoryModel
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist.ServiceListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceSelectionApi {
    @GET("Checkin/getServiceCategory")
    suspend fun serviceCategory(@Query("vendor_id") vendorId: String): ServiceCategoryModel

    @GET("Checkin/getServiceByCategoryId")
    suspend fun getServiceByCategoryId(
        @Query("vendor_id") vendorId: String,
        @Query("category_id") category_id: String
    ): ServiceListModel

}