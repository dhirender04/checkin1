package com.hubwallet.customer_checkin.common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        const val customerId: String = "CUSTOMER_ID"
        const val salonName: String = "SALON_NAME"
        const val salonId: String = "SALON_ID"
        const val vendorKey = "VENDOR_ID"
        const val colorString = "COLOR_SETTING"
        const val keyLogin = "LoginKey"
        const val LoginID = "LoginId"
        const val token = "VENDOR_TOKEN"
        const val name = "CUSTOMER_CHECK_IN"
        const val profilePhoto = "profilePhoto"
        const val tokenRefresh = "REFRESH_TOKEN"
        const val Device_id = "DEVICE_ID"
        const val is_card_booking_available = "IS_CARD_BOOKING_AVAILABLE"
        const val CHECK_IN_ONLINE_BOOKING = "checkin_app_allow"

    }

    var sharedPreferences: SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)

    fun clean() {
        sharedPreferences.edit().clear().apply()

    }

    fun putValueString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getValueString(key: String): String {
        return sharedPreferences.getString(key, "-1") ?: "-1"
    }

}