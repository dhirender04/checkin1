package com.hubwallet.customer_checkin.ui.scan_qr_code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.scan_qr_code.repository.ScanQrCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanQrCodeViewModel @Inject constructor(val repository: ScanQrCodeRepository) : ViewModel() {
    private val qrCheckInMutableLiveData = MutableLiveData<NetworkResponse<BaseResponse>>()
    fun qrCodeCheckIn(customerId: String, vendorId: String) {
        qrCheckInMutableLiveData.postValue(NetworkResponse.Loading(""))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.qrCheckIn(customerId, vendorId)
                qrCheckInMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {

                e.printStackTrace()
                qrCheckInMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "qrCodeScanError"
                    )
                )
            }
        }
    }

    val qrCodeScanLiveData = qrCheckInMutableLiveData as LiveData<NetworkResponse<BaseResponse>>

}