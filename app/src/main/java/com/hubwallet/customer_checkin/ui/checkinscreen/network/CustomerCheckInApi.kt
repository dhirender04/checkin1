package com.hubwallet.customer_checkin.ui.checkinscreen.network

import com.hubwallet.customer_checkin.common.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerCheckInApi {
    @GET("Checkin/getCheckPhoneExist")
    suspend fun customerCheckInNumber(
        @Query("mobile_phone") mobile_phone: String,
        @Query("vendor_id") vendor_id: String,
        @Query("is_checkin") is_checkin: String,
    ): BaseResponse

}