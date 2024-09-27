package com.hubwallet.customer_checkin.ui.book_appointment.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.repository.CheckoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CheckoutFragmentViewModel @Inject constructor(val checkoutRepository: CheckoutRepository) :
    ViewModel() {

    private val addAppointmentLiveData = MutableLiveData<NetworkResponse<BaseResponse>>()


    fun addAppointment(
        json: String
    ) {

        viewModelScope.launch {
            addAppointmentLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = checkoutRepository.addAppointment(json)
                addAppointmentLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                addAppointmentLiveData.postValue(NetworkResponse.Failed(e.localizedMessage ?: ""))
            }

        }

    }


    private val cardLiveData = MutableLiveData<NetworkResponse<CardResponse>>()
    fun getCardList(customerId: String, vendorId: String) {
        cardLiveData.value = NetworkResponse.Loading("")
        viewModelScope.launch {
            try {
                val response = checkoutRepository.getCard(customerId, vendorId)
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
       jsonObject: String
    ) {
        addCardLiveData.value = NetworkResponse.Loading("")
        viewModelScope.launch {
            try {
                val response = checkoutRepository.addCard(jsonObject)
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


    val getAddCardLiveData
        get() = addCardLiveData as LiveData<NetworkResponse<BaseResponse>>
    val getcardLiveData
        get() = cardLiveData as LiveData<NetworkResponse<CardResponse>>

    val getAddAppointmentLiveData
        get() = addAppointmentLiveData as LiveData<NetworkResponse<BaseResponse>>
}