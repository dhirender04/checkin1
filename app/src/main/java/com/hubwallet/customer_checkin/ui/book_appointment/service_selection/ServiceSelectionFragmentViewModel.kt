package com.hubwallet.customer_checkin.ui.book_appointment.service_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.ServiceCategoryModel
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist.ServiceListModel
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.repository.ServiceSelectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceSelectionFragmentViewModel @Inject constructor(private val repository: ServiceSelectionRepository) :
    ViewModel() {
    private val serviceCategoryMutableLiveData =
        MutableLiveData<NetworkResponse<ServiceCategoryModel>>()

    fun serviceCategory(vendorId: String) {
        viewModelScope.launch {
            serviceCategoryMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = repository.serviceCategory(vendorId)
                serviceCategoryMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                serviceCategoryMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "service Category"
                    )
                )
            }
        }


    }

    private val serviceListByCategoryIdMutableLiveData = MutableLiveData<NetworkResponse<ServiceListModel>>()
    fun serviceListByCategoryId(vendorId: String, category_id: String) {
        viewModelScope.launch {
            serviceListByCategoryIdMutableLiveData.postValue(NetworkResponse.Loading(""))
            try {
                val response = repository.getServiceByCategoryId(vendorId, category_id)
                serviceListByCategoryIdMutableLiveData.postValue(NetworkResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                serviceListByCategoryIdMutableLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "serviceByCategory"
                    )
                )
            }
        }
    }

    val serviceListByCategoryId get() = serviceListByCategoryIdMutableLiveData as LiveData<NetworkResponse<ServiceListModel>>

    val serviceCategoryLiveData
        get() = serviceCategoryMutableLiveData as LiveData<NetworkResponse<ServiceCategoryModel>>
}