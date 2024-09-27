package com.hubwallet.customer_checkin.common.utils

import com.google.gson.annotations.SerializedName


data class BaseResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0,

    )