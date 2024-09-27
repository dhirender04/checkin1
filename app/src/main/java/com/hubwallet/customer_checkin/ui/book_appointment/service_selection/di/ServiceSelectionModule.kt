package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.di

import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.ServiceSelectionApi
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.repository.ServiceSelectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class ServiceSelectionModule {
    @Provides
    fun providersServiceSelectionApi(retrofit: Retrofit):ServiceSelectionApi{
        return retrofit.create(ServiceSelectionApi::class.java)
    }
    @Provides
    fun providersServiceSelectionRepository(api: ServiceSelectionApi):ServiceSelectionRepository{
        return ServiceSelectionRepository(api)
    }

}