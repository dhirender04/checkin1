package com.hubwallet.customer_checkin.common.utils

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenticatorTokenAPi {
    @GET("genrate_token/genrateToken")
    suspend fun generateToken(@Header("Authorization") token: String): Response<String>

}