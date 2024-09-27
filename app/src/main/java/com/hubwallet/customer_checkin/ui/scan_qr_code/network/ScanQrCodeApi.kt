package com.hubwallet.customer_checkin.ui.scan_qr_code.network

import com.hubwallet.customer_checkin.common.utils.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ScanQrCodeApi {
    @POST("customer/checkinAppointment")
    @FormUrlEncoded
    suspend fun qrCheckIn(
        @Field("customer_id") customerId: String,
        @Field("vendor_id") vendor_id: String
    ):BaseResponse

}