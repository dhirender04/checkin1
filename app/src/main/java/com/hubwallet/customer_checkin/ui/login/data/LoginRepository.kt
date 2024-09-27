package com.hubwallet.customer_checkin.ui.login.data

import com.hubwallet.customer_checkin.ui.login.network.LoginApi
import javax.inject.Inject

class LoginRepository @Inject constructor ( private val loginApi: LoginApi) {
    suspend fun getLogin(pin:String,device_id:String) = loginApi.getLogin(pin,device_id)
}