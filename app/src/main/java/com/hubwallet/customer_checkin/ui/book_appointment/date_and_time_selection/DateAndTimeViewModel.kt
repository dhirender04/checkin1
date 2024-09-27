package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.StylistTime
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.repository.DateTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DateAndTimeViewModel @Inject constructor(
    private val repository: DateTimeRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val availableTimeStylistLiveData = MutableLiveData<NetworkResponse<StylistTime>>()

    fun availableTimeByStylist(
        vendor_id: String,
        stylist_id: String,
        date: String
    ) {
        availableTimeStylistLiveData.postValue(NetworkResponse.Loading(""))
        viewModelScope.launch {
            val st = if (stylist_id.isEmpty()) "0" else stylist_id
            try {
                val response = repository.getAvailableTimeByStylist(
                    vendor_id,
                    st,
                    date,
                    "2046"
//                    preferenceManager.getValueString(PreferenceManager.customerId)
                )
                availableTimeStylistLiveData.postValue(NetworkResponse.Success(response))

            } catch (e: Exception) {
                e.printStackTrace()
                availableTimeStylistLiveData.postValue(
                    NetworkResponse.Failed(
                        e.localizedMessage ?: "time_error"
                    )
                )
            }
        }
    }

    val getAvailableStylistLiveData get() = availableTimeStylistLiveData as LiveData<NetworkResponse<StylistTime>>
}