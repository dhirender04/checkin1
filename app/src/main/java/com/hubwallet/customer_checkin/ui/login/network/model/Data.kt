package com.hubwallet.customer_checkin.ui.login.network.model

import com.google.gson.annotations.SerializedName

data class Data(
    val email: String,
    val fcm_token: String,
    val is_accept_tmc: String,
    val is_active: String,
    val is_delete: String,
    val is_tmc_show: String,
    val last_login: String,
    val login_id: String,
    val photo: String?,
    val role_id: String,
    val username: String?,
    val vendor_id: String?,


)