package com.hubwallet.customer_checkin.ui.checkinscreen.repository

import com.hubwallet.customer_checkin.ui.checkinscreen.network.CustomerCheckInApi
import javax.inject.Inject

class CustomerCheckInRepository @Inject constructor(val api: CustomerCheckInApi) {
    suspend fun existingCustomerNumber(mobile_no: String, vendor_id: String,is_checkin:String) =
        api.customerCheckInNumber(mobile_no, vendor_id,is_checkin)
}