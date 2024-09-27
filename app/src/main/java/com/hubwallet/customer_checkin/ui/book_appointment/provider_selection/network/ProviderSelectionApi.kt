package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ProviderSelectionApi {
    @GET("Checkin/getStylist")
    suspend fun getStylistList(
        @Query("vendor_id") vendor_id: String,
        @Query("type") type: String,
        @Query("service") service: String,
        @Query("start_date") start_date: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String
    ): String
}