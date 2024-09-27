package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.repository.ProviderSelectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProviderSelectionViewModel @Inject constructor(val providerRepository: ProviderSelectionRepository) :
    ViewModel() {
    private val providerMutableLiveData = MutableLiveData<NetworkResponse<String>>()

    fun providerList(
        vendor_id: String,
        type: String,
        service: String,
        start_date: String,
        start_time: String,
        end_time: String
    ) {
        viewModelScope.launch {
            providerMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = providerRepository.getProviderList(
                    vendor_id,
                    type,
                    service,
                    start_date,
                    start_time,
                    end_time
                )
                providerMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                providerMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "provider_list_error"
                    )
                )

            }
        }
    }

    val providerListLiveData = providerMutableLiveData as LiveData<NetworkResponse<String>>
}