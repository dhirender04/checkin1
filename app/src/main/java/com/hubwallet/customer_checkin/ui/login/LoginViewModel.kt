package com.hubwallet.customer_checkin.ui.login

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.login.data.LoginRepository
 import com.hubwallet.customer_checkin.ui.login.network.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(val repository: LoginRepository) : ViewModel() {

    val builder = StringBuilder()
    val pinMutableLiveData = MutableLiveData<String>()
    private val loginMutableLiveData = MutableLiveData<NetworkResponse<LoginResponse>>()


    //api
    fun loginApi(pin: String, device_id: String) {
        loginMutableLiveData.postValue(NetworkResponse.Loading(""))

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getLogin(pin, device_id)
                loginMutableLiveData.postValue(NetworkResponse.Success(response))

            } catch (e: Exception) {
                loginMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "Login Error"
                    )
                )
                e.printStackTrace()
            }
        }

    }


    //keyboard
    fun updateString(v: View) {
        val button = v as TextView
        Log.e("updateString87878100 ", button.text.toString())
        if (builder.length<10){
            builder.append(button.text.toString())
            pinMutableLiveData.value = builder.toString()
        }
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


    val loginLiveData = loginMutableLiveData as LiveData<NetworkResponse.Success<LoginResponse>>

    val loginPinLiveData
        get() = pinMutableLiveData as LiveData<String>

}