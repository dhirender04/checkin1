package com.hubwallet.customer_checkin.ui.existing_customer.repository

import com.hubwallet.customer_checkin.ui.existing_customer.network.ExistingCustomerCheckInApi
import javax.inject.Inject

class ExistingCustomerCheckInRepository @Inject constructor(val api: ExistingCustomerCheckInApi) {
    suspend fun existingCustomerNumber(mobile_no: String,vendor_id:String,is_checkin:String) = api.existingCustomerNumber(mobile_no,vendor_id,is_checkin)
}