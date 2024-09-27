package com.hubwallet.customer_checkin.ui.existing_customer

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.existing_customer.network.model.ExistingCustomerModel
import com.hubwallet.customer_checkin.ui.existing_customer.repository.ExistingCustomerCheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExistingCustomerCheckInViewModel @Inject constructor(val repository: ExistingCustomerCheckInRepository) :
    ViewModel() {
    val builder = StringBuilder()
    val pinMutableLiveData = MutableLiveData<String>()

    fun updateString(v: View) {

        val button = v as TextView
        Log.e("updateString87878100 ", button.text.toString())
        builder.append(button.text.toString())
        pinMutableLiveData.value = builder.toString()
    }

    fun removePin(v: View) {
        if (builder.isEmpty()) {
            return
        }
        val s: String = builder.subSequence(0, builder.length - 1).toString()
        builder.clear()
        builder.append(s)
        pinMutableLiveData.value = builder.toString()

    }

    val pinLiveData
        get() = pinMutableLiveData as LiveData<String>


    private val existingCustomerPhoneNoMutableLiveData =
        MutableLiveData<NetworkResponse<ExistingCustomerModel>>()

    //apis
    fun existingCustomerCheckIn(mobileNo: String, vendor: String,is_checkin:String) {
        viewModelScope.launch {
            existingCustomerPhoneNoMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = repository.existingCustomerNumber(mobileNo, vendor,is_checkin)
                existingCustomerPhoneNoMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                existingCustomerPhoneNoMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: ""
                    )
                )
            }
        }
    }
    val existingCustomerMobileNoLiveData =
        existingCustomerPhoneNoMutableLiveData as LiveData<NetworkResponse<ExistingCustomerModel>>
}