package com.hubwallet.customer_checkin.ui.existing_customer.di

import com.hubwallet.customer_checkin.ui.existing_customer.network.ExistingCustomerCheckInApi
import com.hubwallet.customer_checkin.ui.existing_customer.repository.ExistingCustomerCheckInRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class ExistingCustomerCheckInModule {
    @Provides
    fun providersExistingCustomerCheckInApi(retrofit: Retrofit):ExistingCustomerCheckInApi{
        return retrofit.create(ExistingCustomerCheckInApi::class.java)
    }
    @Provides
    fun providersExistingCustomerCheckInRepository(api: ExistingCustomerCheckInApi):ExistingCustomerCheckInRepository{
        return ExistingCustomerCheckInRepository(api)
    }
}