package com.hubwallet.customer_checkin.ui.cancelation.network

import com.hubwallet.customer_checkin.ui.cancelation.network.model.CancellationModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CancellationApi {
    @POST("Checkin/policies")
    @FormUrlEncoded
    suspend fun cancellationApi(
        @Field("vendor_id") vendor_id: String,
    ): CancellationModel

}