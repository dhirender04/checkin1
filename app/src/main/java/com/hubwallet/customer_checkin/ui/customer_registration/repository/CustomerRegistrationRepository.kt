package com.hubwallet.customer_checkin.ui.customer_registration.repository

import com.hubwallet.customer_checkin.ui.customer_registration.network.CustomerRegistrationApi
import javax.inject.Inject

class CustomerRegistrationRepository @Inject constructor(private val customerRegistrationApi: CustomerRegistrationApi) {
    suspend fun customerRegistration(registrationJson:String) = customerRegistrationApi.customerRegistration(registrationJson)
    suspend fun getCard(customerId: String, vendorId: String) =
        customerRegistrationApi.getCards(customerId, vendorId)

    suspend fun addCard(
        customerId: String,
        cardHolderName: String,
        cardNumber: String,
        cvv: String,
        expiryMonth: String,
        expiryYear: String,
        cardType: String,
        zip: String?,

        ) =
        customerRegistrationApi.addCard(
            customerId,
            cardHolderName,
            cardNumber,
            cvv,
            expiryMonth,
            expiryYear,
            cardType,
            zip
        )
}