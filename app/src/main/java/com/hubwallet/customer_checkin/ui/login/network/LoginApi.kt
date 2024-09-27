package com.hubwallet.customer_checkin.ui.login.network

import com.hubwallet.customer_checkin.ui.login.network.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi {
    @POST("login/loginPin")
    @FormUrlEncoded
    suspend fun getLogin(
        @Field("pin") pin: String,
        @Field("device_id") device_id: String
    ): LoginResponse
}