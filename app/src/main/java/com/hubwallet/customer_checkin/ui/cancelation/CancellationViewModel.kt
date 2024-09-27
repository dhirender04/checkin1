package com.hubwallet.customer_checkin.ui.cancelation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.cancelation.network.model.CancellationModel
import com.hubwallet.customer_checkin.ui.cancelation.repository.CancellationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancellationViewModel @Inject constructor(val repository: CancellationRepository) :
    ViewModel() {

    private val cancellationMutableLiveData =
        MutableLiveData<NetworkResponse<CancellationModel>>()

    //apis
    fun cancellationApi(vendor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cancellationMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = repository.cancellationApi(vendor)
                cancellationMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                cancellationMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: ""
                    )
                )
            }
        }
    }

    val cancellationLiveData =
        cancellationMutableLiveData as LiveData<NetworkResponse<CancellationModel>>

}