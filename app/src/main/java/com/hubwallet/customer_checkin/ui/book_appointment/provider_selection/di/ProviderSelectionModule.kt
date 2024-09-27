package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.di

import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.network.ProviderSelectionApi
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.repository.ProviderSelectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class ProviderSelectionModule {
    @Provides
    fun providerProviderSelectionApi(retrofit: Retrofit): ProviderSelectionApi {
        return retrofit.create(ProviderSelectionApi::class.java)
    }

    @Provides
    fun providerProviderSelectionRepository(api: ProviderSelectionApi): ProviderSelectionRepository {
        return ProviderSelectionRepository((api))
    }


}