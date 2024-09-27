package com.hubwallet.customer_checkin.ui.existing_customer.network

import com.hubwallet.customer_checkin.ui.existing_customer.network.model.ExistingCustomerModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ExistingCustomerCheckInApi {
    @GET("Checkin/getCheckPhoneExist")
    suspend fun existingCustomerNumber(
        @Query("mobile_phone") mobile_phone: String,
        @Query("vendor_id") vendor_id: String,
        @Query("is_checkin") is_checkin: String
    ): ExistingCustomerModel

}