package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.repository

import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.DateTimeApi
import javax.inject.Inject

class DateTimeRepository @Inject constructor(val api: DateTimeApi) {
    suspend fun getAvailableTimeByStylist(
        vendor_id: String,
        stylist_id: String,
        date: String,
        id: String
    ) = api.getAvailableTimeByStylist(vendor_id, stylist_id, date, id)
}