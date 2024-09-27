package com.hubwallet.customer_checkin.common.utils

class DecimalFormatterAmount {
    companion object {
        fun formatToTwoDecimalPlaces(float: Float): String {
            return String.format("%.2f", float)
        }

    }
}