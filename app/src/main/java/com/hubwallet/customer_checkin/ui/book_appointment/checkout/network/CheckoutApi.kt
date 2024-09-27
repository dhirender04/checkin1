package com.hubwallet.customer_checkin.ui.book_appointment.checkout.network

import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckoutApi {
    @POST("Checkin/addAppointment")
    suspend fun addAppointment(
        @Body jsonObject: String
    ): BaseResponse

    @GET("customer/getCardByCustomerId")
    suspend fun getCards(
        @Query("customer_id") customerId: String,
        @Query("vendor_id") vendor_id: String,
    ): CardResponse

    @POST("customer/addNewCard")
    suspend fun addCard(
        @Body jsonObject: String
    ): BaseResponse
}