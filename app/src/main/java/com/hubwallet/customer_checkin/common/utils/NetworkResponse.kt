package com.hubwallet.customer_checkin.common.utils

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Failed<out T>(val error: String) : NetworkResponse<T>()
    data class Loading<T>(val message: String) : NetworkResponse<T>()
}
