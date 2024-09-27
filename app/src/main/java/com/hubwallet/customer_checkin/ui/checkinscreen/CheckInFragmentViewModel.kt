package com.hubwallet.customer_checkin.ui.checkinscreen

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.checkinscreen.repository.CustomerCheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInFragmentViewModel @Inject constructor(val repository: CustomerCheckInRepository)  : ViewModel() {

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


    private val customerPhoneNoMutableLiveData =
        MutableLiveData<NetworkResponse<BaseResponse>>()

    //apis
    fun customerCheckInNumber(mobileNo: String, vendor: String,is_checkin:String
    ) {
        viewModelScope.launch {
            customerPhoneNoMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = repository.existingCustomerNumber(mobileNo, vendor,is_checkin)
                customerPhoneNoMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                customerPhoneNoMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: ""
                    )
                )
            }
        }
    }

    val customerMobileNoLiveData =
        customerPhoneNoMutableLiveData as LiveData<NetworkResponse<BaseResponse>>

}