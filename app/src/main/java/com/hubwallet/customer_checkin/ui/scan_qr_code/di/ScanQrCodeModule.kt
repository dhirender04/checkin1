package com.hubwallet.customer_checkin.ui.scan_qr_code.di

import com.hubwallet.customer_checkin.ui.scan_qr_code.network.ScanQrCodeApi
import com.hubwallet.customer_checkin.ui.scan_qr_code.repository.ScanQrCodeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class ScanQrCodeModule {
    @Provides
    fun providersScanQrCodeApi(retrofit: Retrofit): ScanQrCodeApi {
        return retrofit.create(ScanQrCodeApi::class.java)
    }

    @Provides
    fun providersScanQrCodeRepository(api: ScanQrCodeApi): ScanQrCodeRepository {
        return ScanQrCodeRepository(api)
    }
}