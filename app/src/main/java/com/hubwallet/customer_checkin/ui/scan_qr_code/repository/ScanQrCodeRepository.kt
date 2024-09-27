package com.hubwallet.customer_checkin.ui.scan_qr_code.repository

import com.hubwallet.customer_checkin.ui.scan_qr_code.network.ScanQrCodeApi
import javax.inject.Inject

class ScanQrCodeRepository @Inject constructor(private val scanQrCodeApi: ScanQrCodeApi) {
    suspend fun qrCheckIn(customerId: String, vendorId: String) =
        scanQrCodeApi.qrCheckIn(customerId, vendorId)
}