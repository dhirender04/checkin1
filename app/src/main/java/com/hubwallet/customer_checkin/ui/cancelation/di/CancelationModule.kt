package com.hubwallet.customer_checkin.ui.cancelation.di

import com.hubwallet.customer_checkin.ui.cancelation.network.CancellationApi
import com.hubwallet.customer_checkin.ui.cancelation.repository.CancellationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class CancelationModule {
    @Provides
    fun providersCancellationApi(retrofit: Retrofit): CancellationApi {
        return retrofit.create(CancellationApi::class.java)
    }

    @Provides
    fun providersCancellationRepository(api: CancellationApi): CancellationRepository {
        return CancellationRepository(api)
    }
}