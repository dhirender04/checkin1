package com.hubwallet.customer_checkin.ui.customer_registration.network

import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import com.hubwallet.customer_checkin.ui.customer_registration.network.model.CustomerRegistrationModel
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CustomerRegistrationApi {
    @POST("Checkin/addnewCustomer")
    suspend fun customerRegistration(@Body jsonObject: String): CustomerRegistrationModel
    @GET("customer/getCardByCustomerId")
    suspend fun getCards(
        @Query("customer_id") customerId: String,
        @Query("vendor_id") vendor_id: String,
    ): CardResponse

    @POST("customer/addNewCard")
    @FormUrlEncoded
    suspend fun addCard(
        @Field("customer_id") customerId: String,
        @Field("card_holder_name") cardHolderName: String,
        @Field("card_number") cardNumber: String,
        @Field("cvv") cvv: String,
        @Field("expiry_month") expiryMonth: String,
        @Field("expiry_year") expiryYear: String,
        @Field("card_type") cardType: String,
        @Field("zipcode") zip: String?,
    ): BaseResponse
}