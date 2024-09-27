package com.hubwallet.customer_checkin.ui.customer_registration.di

import com.hubwallet.customer_checkin.ui.customer_registration.network.CustomerRegistrationApi
import com.hubwallet.customer_checkin.ui.customer_registration.repository.CustomerRegistrationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
object CustomerRegistrationModule {
    @Provides
    fun providesCustomerRegistrationApi(retrofit: Retrofit): CustomerRegistrationApi {
        return retrofit.create(CustomerRegistrationApi::class.java)
    }
    @Provides
    fun providesCustomerRegistrationRepository(customerRegistrationApi: CustomerRegistrationApi):CustomerRegistrationRepository{
        return CustomerRegistrationRepository(customerRegistrationApi)

    }
}
