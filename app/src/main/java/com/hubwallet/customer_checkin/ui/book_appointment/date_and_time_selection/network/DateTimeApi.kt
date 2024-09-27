package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network

import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.StylistTime
import retrofit2.http.*

interface DateTimeApi {
    @POST("Checkin/getAvailableTimeByStylist")
    @FormUrlEncoded
    suspend fun getAvailableTimeByStylist(
        @Field("vendor_id") vendor_id: String,
        @Field("stylist_id") stylist_id: String,
        @Field("date") date: String,
        @Field("customer_id") customer: String,
    ): StylistTime
}