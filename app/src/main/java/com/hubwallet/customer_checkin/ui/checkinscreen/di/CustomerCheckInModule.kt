package com.hubwallet.customer_checkin.ui.checkinscreen.di

import com.hubwallet.customer_checkin.ui.checkinscreen.network.CustomerCheckInApi
import com.hubwallet.customer_checkin.ui.checkinscreen.repository.CustomerCheckInRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class CustomerCheckInModule {
    @Provides
    fun providersCustomerCheckInApi(retrofit: Retrofit): CustomerCheckInApi {
        return retrofit.create(CustomerCheckInApi::class.java)
    }

    @Provides
    fun providersCustomerCheckInRepository(api: CustomerCheckInApi): CustomerCheckInRepository {
        return CustomerCheckInRepository(api)
    }
}