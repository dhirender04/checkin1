package com.hubwallet.customer_checkin.ui.book_appointment.checkout.repository

import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.CheckoutApi
import javax.inject.Inject

class CheckoutRepository @Inject constructor(val checkoutApi: CheckoutApi) {
    suspend fun addAppointment(jsonObject: String) = checkoutApi.addAppointment(jsonObject)

    suspend fun getCard(customerId: String, vendorId: String) =
        checkoutApi.getCards(customerId, vendorId)

    suspend fun addCard(jsonObject: String) =
        checkoutApi.addCard(
           jsonObject
        )
}