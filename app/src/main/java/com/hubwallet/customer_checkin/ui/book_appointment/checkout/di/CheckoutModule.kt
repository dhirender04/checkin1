package com.hubwallet.customer_checkin.ui.book_appointment.checkout.di

import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.CheckoutApi
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.repository.CheckoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class CheckoutModule {
    @Provides
    fun providersCheckoutApi(retrofit: Retrofit): CheckoutApi {
        return retrofit.create(CheckoutApi::class.java)
    }

    @Provides
    fun providersCheckoutRepository(checkoutApi: CheckoutApi): CheckoutRepository {
        return CheckoutRepository(checkoutApi)
    }
}