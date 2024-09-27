package com.hubwallet.customer_checkin.ui.customer_registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import com.hubwallet.customer_checkin.ui.customer_registration.network.model.CustomerRegistrationModel
import com.hubwallet.customer_checkin.ui.customer_registration.repository.CustomerRegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerRegistrationViewModel @Inject constructor(private val customerRegistrationRepository: CustomerRegistrationRepository) :
    ViewModel() {
     private val customerRegistrationMutableLiveData =
        MutableLiveData<NetworkResponse<CustomerRegistrationModel>>()

    fun customerRegistration(jsonObject: String) {
        customerRegistrationMutableLiveData.value = NetworkResponse.Loading("")
        viewModelScope.launch {
            try {
                val response = customerRegistrationRepository.customerRegistration(jsonObject)
                customerRegistrationMutableLiveData.postValue(NetworkResponse.Success(response))

            } catch (e: Exception) {
                e.printStackTrace()
                customerRegistrationMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "customer Registration"
                    )
                )
            }
        }

    }

    private val cardLiveData = MutableLiveData<NetworkResponse<CardResponse>>()
    fun getCardList(customerId: String, vendorId: String) {
        cardLiveData.value = NetworkResponse.Loading("")
        viewModelScope.launch {
            try {
                val response = customerRegistrationRepository.getCard(customerId, vendorId)
                cardLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                cardLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage
                            ?: "error!"
                    )
                )

            }
        }

    }


    private val addCardLiveData = MutableLiveData<NetworkResponse<BaseResponse>>()

    fun addCard(
        customerId: String,
        cardHolderName: String,
        cardNumber: String,
        cvv: String,
        expiryMonth: String,
        expiryYear: String,
        cardType: String,
        zip: String?,
    ) {
        addCardLiveData.value = NetworkResponse.Loading("")
        viewModelScope.launch {
            try {
                val response = customerRegistrationRepository.addCard(
                    customerId,
                    cardHolderName,
                    cardNumber,
                    cvv,
                    expiryMonth,
                    expiryYear,
                    cardType,
                    zip
                )
                addCardLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                addCardLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage
                            ?: "error!"
                    )
                )

            }
        }

    }

    val customerRegistrationLiveData
            get() = customerRegistrationMutableLiveData as LiveData<NetworkResponse<CustomerRegistrationModel>>

    val getAddCardLiveData
        get() = addCardLiveData as LiveData<NetworkResponse<BaseResponse>>
    val getcardLiveData
        get() = cardLiveData as LiveData<NetworkResponse<CardResponse>>

}