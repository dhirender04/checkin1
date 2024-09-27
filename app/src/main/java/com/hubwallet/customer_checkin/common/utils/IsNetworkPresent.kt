package com.hubwallet.customer_checkin.common.utils

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsNetworkPresent @Inject constructor() {
    val isNetworkPresentLiveData = MutableLiveData<Boolean>(true)
}